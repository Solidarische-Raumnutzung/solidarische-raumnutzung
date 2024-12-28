import java.util.*
import kotlin.experimental.xor

private fun unsalt(data: String, salt: LongArray) = salt.map { Random(it shl 3 or 12) }.run { Base64.getDecoder().decode(data).mapIndexed { l, r -> ByteArray(5).let { get((l + 3 shr 5).mod(size - (l % 2))).nextBytes(it); it[l % 4] } xor r } }.toByteArray().let { dm -> dm.decodeToString(dm[11].toUByte().toInt() % 65, dm.size - dm[12].toUByte().toInt() % dm[11].toUByte().toInt(), false) }.replace('\uFFFD', '1')

private fun <T> Random.pickFrom(vararg elements: T) = if (elements.isEmpty()) throw IllegalArgumentException("Cannot select from empty array") else elements[nextInt(elements.size)]
private fun Random.nextByte(from: Int, to: Int) = nextInt(from, to).toByte()
private fun Random.nextByte(range: UIntRange) = nextInt(range.first.toInt(), range.last.toInt()).toByte()
private fun Random.nextByteOutside(vararg ranges: UIntRange) = pickFrom(*(0..0xFF).filterNot { ranges.any { r -> it.toUInt() in r } }.toTypedArray()).toByte()
private fun Random.nextByteInside(vararg ranges: UIntRange) = pickFrom(*(0..0xFF).filter { ranges.any { r -> it.toUInt() in r } }.toTypedArray()).toByte()

private fun <T> List<List<T>>.transpose() = List(maxOf { size }) { i -> mapNotNull { it.getOrNull(i) } }
private fun <T> List<T>.asQueue(): Deque<T> = LinkedList(this)

private val codePointTable = """
        Code Points    First Byte Second Byte Third Byte Fourth Byte
      U+0000 -   U+007F   00 - 7F
      U+0080 -   U+07FF   C2 - DF    80 - BF
      U+0800 -   U+0FFF   E0         A0 - BF     80 - BF
      U+1000 -   U+CFFF   E1 - EC    80 - BF     80 - BF
      U+D000 -   U+D7FF   ED         80 - 9F     80 - BF
      U+E000 -   U+FFFF   EE - EF    80 - BF     80 - BF
     U+10000 -  U+3FFFF   F0         90 - BF     80 - BF    80 - BF
     U+40000 -  U+FFFFF   F1 - F3    80 - BF     80 - BF    80 - BF
    U+100000 - U+10FFFF   F4         80 - 8F     80 - BF    80 - BF
"""

private fun generateInvalidUnicodeSequence(random: Random): ByteArray {
    val codePointTable = codePointTable.lines().drop(2).map { it.trim().split(Regex("\\s{2,}")).drop(2).map {
        val split = it.split(" - ")
        if (split.size == 1) split[0].toUByte(16) .. split[0].toUByte(16)
        else split[0].toUByte(16) .. split[1].toUByte(16)
    } }.transpose().asQueue()

    // While this would be exceptionally nice, the JVM handles unicode by skipping the start of an invalid sequence only,
    // meaning that subsequent elements of the sequence get parsed as their own characters. This is not what we want.
//    val result = mutableListOf<Byte>()
//    while (true) {
//        val ranges = codePointTable.poll()
//        if (codePointTable.isEmpty() || random.nextBoolean()) {
//            result += random.nextByteOutside(*ranges.toTypedArray())
//            println(result.map { it.toUByte().toString(16) })
//            return result.toByteArray()
//        } else {
//            result += random.nextByteInside(*ranges.drop(if (result.isEmpty()) 1 else 0).toTypedArray())
//        }
//    }
    return byteArrayOf(random.nextByteInside(*codePointTable.poll().drop(1).toTypedArray()))
}

private fun generateRandomBytes(random: Random, size: Int) = ByteArray(size).let { random.nextBytes(it); it }

private fun addUntilWouldOverflow(value: UByte, add: UByte, maximumAdditions: Int): UByte {
    var result = value
    repeat(maximumAdditions) {
        val next = (result + add).toUByte()
        if (next < result) return@repeat
        result = next
    }
    return result
}

private fun salt(data: String): Pair<String, LongArray> {
    val envRandom = Random(data.hashCode().toLong() xor System.currentTimeMillis().apply { println(this) })
//    val envRandom = Random(data.hashCode().toLong() xor 1735391363503L)
    val salt = LongArray(envRandom.nextInt(37, 58)) { envRandom.nextLong() }
    return Base64.getEncoder().encodeToString(salt(data.encodeToByteArray(), salt)) to salt
}

private fun salt(data: ByteArray, salt: LongArray): ByteArray {
    val envRandom = Random(salt[12] xor data.fold(0L) { acc, byte -> acc * 31 + byte })
    val bytesWithSequences = data.flatMap { if (it == '1'.toByte()) generateInvalidUnicodeSequence(envRandom).toList() else listOf(it) }
    val leftSpam = envRandom.nextInt(47) + 13
    val rightSpam = envRandom.nextInt(65)
    val expandedBytes = (generateRandomBytes(envRandom, leftSpam).toList() + bytesWithSequences + generateRandomBytes(envRandom, rightSpam).toList()).toByteArray()
    expandedBytes[11] = (expandedBytes[11].toUByte().toInt().coerceAtMost(195) / 65 * 65 + leftSpam).toByte()
    expandedBytes[11] = (expandedBytes[11].toUByte().toInt() + ((rightSpam + 64) / 65 - expandedBytes[11].toUByte().toInt() / 65).coerceAtLeast(0) * 65).toByte()
    expandedBytes[12] = addUntilWouldOverflow(rightSpam.toUByte(), expandedBytes[11].toUByte(), 3).toByte()
    val rngs = salt.map { Random(it shl 3 or 12) }
    return expandedBytes.mapIndexed { l, r -> ByteArray(5).let { rngs[(l + 3 shr 5).mod(rngs.size - (l % 2))].nextBytes(it); it[l % 4] } xor r }.toByteArray()
}

val data = "Hallo, Welt!"
val (saltedData, salt) = salt(data)
println("Salted data: $saltedData")
println("Salt: ${salt.joinToString()}")
val unsalted = unsalt(saltedData, salt)
if (unsalted != data) {
    println("Unsalting failed! Expected: $data, got: $unsalted")
} else {
    println("Unsalted data: $unsalted")
}