plugins {
    id("org.jetbrains.kotlin.jvm") version TrevakaServiceDeps.kotlin
    id("org.jetbrains.kotlin.plugin.spring") version TrevakaServiceDeps.kotlin
    id("org.springframework.boot") version TrevakaServiceDeps.springBoot
}

repositories {
    jcenter()
    mavenCentral()
}


dependencies {
    implementation(":evaka-service")

    implementation("io.github.microutils:kotlin-logging:${TrevakaServiceDeps.kotlinLogging}")
    implementation("net.rakugakibox.spring.boot:logback-access-spring-boot-starter:${TrevakaServiceDeps.logbackSpringBoot}")

    implementation("org.springframework.boot:spring-boot-starter-web")

}

springBoot {
    mainClass.set("fi.tampere.trevaka.TrevakaMainKt")
}

allprojects {
    tasks.withType<JavaCompile> {
        sourceCompatibility = TrevakaServiceDeps.java
        targetCompatibility = TrevakaServiceDeps.java
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = TrevakaServiceDeps.java
        allWarningsAsErrors = name != "compileIntegrationTestKotlin"
    }
}

tasks {
    bootRun {
        systemProperty("spring.profiles.active", "local,trevaka-local")
    }
}
