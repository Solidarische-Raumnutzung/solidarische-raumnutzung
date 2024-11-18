plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("gg.jte.gradle") version "3.1.15"
}

group = "edu.kit.hci"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
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
    implementation("org.flywaydb:flyway-core:10.21.0") // TODO: remove explicit version when Spring Boot 3.4 is released
    implementation("org.flywaydb:flyway-database-postgresql:10.21.0")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("gg.jte:jte:3.1.15")
    implementation("org.springframework.boot:spring-boot-starter-security")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

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
