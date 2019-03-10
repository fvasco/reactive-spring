import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.21"
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

version = "1.0.0-SNAPSHOT"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    val coroutinesVersion = "1.1.1"
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
}
