plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ktlint.gradle)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

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

val xjcTool: Configuration by configurations.creating
val xjcGenerate = tasks.register<JavaExec>("xjcGenerate") {
    group = "code generation"
    description = "Generates Java classes from XML schema using the XJC binding compiler"

    val schemaDirectory = layout.projectDirectory.dir("src/main/schema")
    val outputDirectory = layout.buildDirectory.dir("generated/sources/xjc/java/main")

    classpath = xjcTool
    mainClass = "com.sun.tools.xjc.Driver"
    args = listOf(
        "-no-header",
        "-quiet",
        "-d",
        outputDirectory.get().asFile.absolutePath,
        schemaDirectory.asFile.absolutePath,
    )

    inputs.dir(schemaDirectory)
    outputs.dir(outputDirectory)
}
tasks.compileKotlin {
    dependsOn(xjcGenerate)
}
sourceSets.main {
    java.srcDirs(xjcGenerate)
}

dependencies {
    xjcTool(platform("org.glassfish.jaxb:jaxb-bom:4.0.5"))
    xjcTool("com.sun.xml.bind:jaxb-xjc")
    xjcTool("com.sun.xml.bind:jaxb-impl")

    implementation(platform("evaka:evaka-bom"))
    testImplementation(platform("evaka:evaka-bom"))

    implementation("evaka:evaka-service")

    implementation("ch.qos.logback.access:logback-access-tomcat")
    implementation("io.github.oshai:kotlin-logging-jvm")
    implementation("net.logstash.logback:logstash-logback-encoder")
    implementation("org.thymeleaf:thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")

    implementation("org.apache.httpcomponents.core5:httpcore5")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("com.github.kittinunf.fuel:fuel")
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-json")
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:http-auth-aws-crt")
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("com.github.kagkarlsson:db-scheduler")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    implementation("org.bouncycastle:bcpkix-jdk18on")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("org.apache.tika:tika-core")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit-pioneer:junit-pioneer:2.3.0") // for CartesianProductTest
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("org.springframework.ws:spring-ws-test")
    testImplementation(platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    testImplementation("com.auth0:java-jwt")
}

springBoot {
    mainClass.set("trevaka.TrevakaMainKt")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set(libs.versions.ktlint.asProvider().get())
}
