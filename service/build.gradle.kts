plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    id("org.unbroken-dome.xjc") version "2.0.0"
    alias(libs.plugins.ktlint.gradle)
}

java.sourceCompatibility = JavaVersion.VERSION_21

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

    implementation(platform("evaka:evaka-bom"))
    testImplementation(platform("evaka:evaka-bom"))

    implementation("evaka:evaka-service")

    implementation("ch.qos.logback:logback-access")
    implementation("io.github.microutils:kotlin-logging-jvm")
    implementation("net.logstash.logback:logstash-logback-encoder")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")

    implementation("org.apache.httpcomponents.core5:httpcore5")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("com.github.kittinunf.fuel:fuel")
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("software.amazon.awssdk:s3")
    implementation("io.opentracing:opentracing-util")
    implementation("com.github.kagkarlsson:db-scheduler")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit-pioneer:junit-pioneer:2.2.0") // for CartesianProductTest
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("org.springframework.ws:spring-ws-test")
    testImplementation(platform("org.springframework.cloud:spring-cloud-dependencies:2022.0.4"))
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    testImplementation("org.reflections:reflections:0.10.2")
    testImplementation("com.auth0:java-jwt")
    testImplementation("org.thymeleaf:thymeleaf")
}

springBoot {
    mainClass.set("trevaka.TrevakaMainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    bootRun {
        systemProperty("spring.profiles.include", "local,trevaka-local")
    }
}

allprojects {
    tasks.register("resolveDependencies") {
        description = "Resolves all dependencies"
        doLast {
            configurations
                .matching { it.isCanBeResolved }
                .map {
                    val files = it.resolve()
                    it.name to files.size
                }
                .groupBy({ (_, count) -> count }) { (name, _) -> name }
                .forEach { (count, names) ->
                    println(
                        "Resolved $count dependency files for configurations: ${names.joinToString(", ")}",
                    )
                }
        }
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("0.50.0")
}
