plugins {
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    java
    id("com.github.ben-manes.versions") version "0.53.0"
}

group = "no.novari"
version = "0.0.1-SNAPSHOT"
var apiVersion = "3.21.10"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.named("annotationProcessor").get())
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.fintlabs.no/releases")
    }
}

tasks.jar {
    isEnabled = false
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("no.novari:flyt-resource-server:6.0.0-rc-26")

    implementation("no.novari:kafka:5.0.0-rc-20")

    implementation("no.fint:fint-arkiv-resource-model-java:$apiVersion")
    implementation("no.fint:fint-administrasjon-resource-model-java:$apiVersion")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testRuntimeOnly("com.h2database:h2")
}

tasks.test {
    useJUnitPlatform()
}
