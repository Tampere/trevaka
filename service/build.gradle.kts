plugins {
    id("org.jetbrains.kotlin.jvm") version TrevakaServiceVersion.kotlin
    id("org.jetbrains.kotlin.plugin.spring") version TrevakaServiceVersion.kotlin
    id("org.springframework.boot") version TrevakaServiceVersion.springBoot
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
