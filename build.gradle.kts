plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.flywaydb.flyway") version "11.1.0"
    id("gg.jte.gradle") version "3.1.15"
    id("jacoco")

}

group = "edu.kit.hci"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:11.0.1") // TODO: remove explicit version when the Spring Boot version is updated
    implementation("org.flywaydb:flyway-database-postgresql:11.1.0")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("gg.jte:jte:3.1.15")
    implementation("org.springframework.boot:spring-boot-starter-security")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    compileOnly("org.jetbrains:annotations:26.0.1")

    // https://docs.spring.io/spring-boot/reference/features/dev-services.html#features.dev-services.docker-compose
    testAndDevelopmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jte {
    generate()
    binaryStaticContent = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val copyArtifact by tasks.creating(Copy::class) {
    dependsOn(tasks.bootJar)
    from(tasks.bootJar)
    into(layout.buildDirectory.dir("libs"))
    rename(".*\\.jar", "app.jar")
}
tasks.assemble {
    dependsOn(copyArtifact)
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}
