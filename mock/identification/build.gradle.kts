// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.freefair.lombok") version "8.11"
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/releases")
    maven(url = "https://build.shibboleth.net/nexus/content/repositories/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(platform("org.apereo.cas:cas-server-support-bom:7.1.3"))
    implementation("org.apereo.cas:cas-server-webapp-starter-tomcat")

    implementation("org.apereo.cas:cas-server-support-generic")
    implementation("org.apereo.cas:cas-server-support-yaml-service-registry")
    implementation("org.apereo.cas:cas-server-support-saml-idp")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("org.apereo.cas.web.CasWebApplication")
}
