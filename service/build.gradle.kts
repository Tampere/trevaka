plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    id("org.unbroken-dome.xjc") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://build.shibboleth.net/maven/releases") {
        content {
            includeGroup("net.shibboleth")
            includeGroup("net.shibboleth.utilities")
            includeGroup("org.opensaml")
        }
    }
}

dependencies {
    xjcTool("com.sun.xml.bind:jaxb-xjc:3.0.2")
    xjcTool("com.sun.xml.bind:jaxb-impl:3.0.2")

    implementation(platform(":evaka-bom"))
    testImplementation(platform(":evaka-bom"))

    implementation(":evaka-service")

    implementation("ch.qos.logback:logback-access")
    implementation("io.github.microutils:kotlin-logging-jvm")
    implementation("net.logstash.logback:logstash-logback-encoder")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")

    implementation("org.apache.httpcomponents:httpclient:4.5.14") {
        exclude("commons-logging", "commons-logging")
    }
    implementation("com.github.kittinunf.fuel:fuel")
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("software.amazon.awssdk:s3")
    implementation("io.opentracing:opentracing-util")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit-pioneer:junit-pioneer:2.0.0") // for CartesianProductTest
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("org.springframework.ws:spring-ws-test")
    testImplementation(platform("org.springframework.cloud:spring-cloud-dependencies:2022.0.1"))
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    testImplementation("org.reflections:reflections:0.10.2")
    testImplementation("com.auth0:java-jwt")
    testImplementation("org.thymeleaf:thymeleaf")
    testImplementation("com.networknt:json-schema-validator:1.0.72")
    testImplementation("org.skyscreamer:jsonassert")
}

springBoot {
    mainClass.set("fi.tampere.trevaka.TrevakaMainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    test {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "integration-test,trevaka")
    }
    bootRun {
        systemProperty("spring.profiles.active", "local,trevaka-local")
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("0.48.2")
}
