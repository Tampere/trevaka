plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.4.21"
    id("org.springframework.boot") version "2.4.1"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(":evaka-service")
}

tasks {
    bootRun {
        systemProperty("spring.profiles.active", "local")
    }
}
