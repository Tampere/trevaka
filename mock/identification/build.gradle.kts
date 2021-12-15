import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.freefair.lombok") version "6.3.0"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/releases")
    maven(url = "https://build.shibboleth.net/nexus/content/repositories/releases/")
}

dependencyManagement {
    imports {
        mavenBom("org.apereo.cas:cas-server-support-bom:6.4.4")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // minimal cas installation from https://github.com/apereo/cas/blob/v6.4.4/gradle/webapp.gradle
    implementation("org.apereo.cas:cas-server-core")
    implementation("org.apereo.cas:cas-server-core-audit")
    implementation("org.apereo.cas:cas-server-core-authentication")
    implementation("org.apereo.cas:cas-server-core-configuration")
    implementation("org.apereo.cas:cas-server-core-cookie")
    implementation("org.apereo.cas:cas-server-core-logout")
    implementation("org.apereo.cas:cas-server-core-logging")
    implementation("org.apereo.cas:cas-server-core-services")
    implementation("org.apereo.cas:cas-server-core-tickets")
    implementation("org.apereo.cas:cas-server-core-util")
    implementation("org.apereo.cas:cas-server-core-validation")
    implementation("org.apereo.cas:cas-server-core-web")
    implementation("org.apereo.cas:cas-server-core-notifications")
    implementation("org.apereo.cas:cas-server-support-jpa-util")
    implementation("org.apereo.cas:cas-server-support-actions")
    implementation("org.apereo.cas:cas-server-support-person-directory")
    implementation("org.apereo.cas:cas-server-support-themes")
    implementation("org.apereo.cas:cas-server-support-validation")
    implementation("org.apereo.cas:cas-server-support-thymeleaf")
    implementation("org.apereo.cas:cas-server-support-pm-webflow")
    implementation("org.apereo.cas:cas-server-webapp-config")
    implementation("org.apereo.cas:cas-server-webapp-init")
    implementation("org.apereo.cas:cas-server-webapp-resources")
    implementation("org.apereo.cas:cas-server-webapp-init-tomcat")

    implementation("org.apereo.cas:cas-server-support-generic")
    implementation("org.apereo.cas:cas-server-support-yaml-service-registry")
    implementation("org.apereo.cas:cas-server-support-saml-idp")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("org.apereo.cas.web.CasWebApplication")
}
