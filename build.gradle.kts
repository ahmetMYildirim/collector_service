plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "collector_service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.kafka:kafka-streams")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("net.dv8tion:JDA:5.0.0") // Discord API
    // Zoom API - REST Client
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("io.jsonwebtoken:jjwt:0.13.0") //Token handling
    testImplementation("com.h2database:h2")
    implementation("mysql:mysql-connector-java:8.0.33") // mySQL connecter
    implementation("com.google.cloud:google-cloud-vertexai:1.1.0")
    implementation("org.bytedeco:ffmpeg:7.1.1-1.5.12")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    runImage = "paketobuildpacks/ubuntu-noble-run-base:latest"
}
