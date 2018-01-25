import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.0.M7")
    }
}

apply {
    plugin("org.springframework.boot")
}

plugins {
    val kotlinVersion = "1.2.21"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.3.RELEASE"
}

version = "1.0.0-SNAPSHOT"

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xcoroutines=enable")
        }
    }
}

repositories {
    mavenCentral()
    maven("http://repo.spring.io/milestone")
}

dependencies {
    val coroutinesVersion = "0.22.2"
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
}

