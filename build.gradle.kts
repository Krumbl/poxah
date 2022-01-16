import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
    kotlin("plugin.serialization") version "1.6.10"
}

group = "poxah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "1.6.7"
dependencies {
    testImplementation(kotlin("test"))
//    implementation("org.slf4j:slf4j-api:1.7.33")
//    implementation("org.slf4j:slf4j-simple:1.7.33")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
//    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("MainKt")
}