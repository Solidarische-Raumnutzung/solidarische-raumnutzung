package edu.kit.hci.soli.test.domain;

import edu.kit.hci.soli.domain.Room;
import edu.kit.hci.soli.domain.TimeTuple;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testGetOpeningHours_AllWeekdaysPresent() {
        Room room = new Room();
        Map<DayOfWeek, TimeTuple> openingHours = new HashMap<>();
        openingHours.put(DayOfWeek.MONDAY, new TimeTuple());
        openingHours.put(DayOfWeek.TUESDAY, new TimeTuple());
        room.setOpeningHours(openingHours);

        Map<DayOfWeek, TimeTuple> result = room.getOpeningHours();
        assertEquals(5, result.size());
        assertTrue(result.containsKey(DayOfWeek.MONDAY));
        assertTrue(result.containsKey(DayOfWeek.TUESDAY));
        assertTrue(result.containsKey(DayOfWeek.WEDNESDAY));
        assertTrue(result.containsKey(DayOfWeek.THURSDAY));
        assertTrue(result.containsKey(DayOfWeek.FRIDAY));
    }

    @Test
    void testGetOpeningHours_EmptyMap() {
        Room room = new Room();
        room.setOpeningHours(new HashMap<>());

        Map<DayOfWeek, TimeTuple> result = room.getOpeningHours();
        assertEquals(5, result.size());
        assertTrue(result.containsKey(DayOfWeek.MONDAY));
        assertTrue(result.containsKey(DayOfWeek.TUESDAY));
        assertTrue(result.containsKey(DayOfWeek.WEDNESDAY));
        assertTrue(result.containsKey(DayOfWeek.THURSDAY));
        assertTrue(result.containsKey(DayOfWeek.FRIDAY));
    }

    @Test
    void testEquals_SameId_ReturnsTrue() {
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(1L);

        assertTrue(room1.equals(room2));
    }

    @Test
    void testEquals_DifferentId_ReturnsFalse() {
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        assertFalse(room1.equals(room2));
    }

    @Test
    void testEquals_SameIdDifferentAttributes_ReturnsTrue() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Room A");
        Room room2 = new Room();
        room2.setId(1L);
        room2.setName("Room B");

        assertTrue(room1.equals(room2));
    }

    @Test
    void testEquals_NullObject_ReturnsFalse() {
        Room room = new Room();
        room.setId(1L);

        assertFalse(room.equals(null));
    }

    @Test
    void testEquals_DifferentClass_ReturnsFalse() {
        Room room = new Room();
        room.setId(1L);
        Object obj = new Object();

        assertFalse(room.equals(obj));
    }

    @Test
    void testHashCode_SameId_SameHashCode() {
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(1L);

        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    void testHashCode_DifferentId_DifferentHashCode() {
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);

        assertNotEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    public void testGetName() {
        Room room = new Room();
        room.setName("Room A");
        assertEquals("Room A", room.getName());
    }

    @Test
    public void testGetLocation() {
        Room room = new Room();
        room.setLocation("Location A");
        assertEquals("Location A", room.getLocation());
    }
}