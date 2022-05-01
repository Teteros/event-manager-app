import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"

    // Dokka - KDOC documentation tool
    id("org.jetbrains.dokka") version "1.6.10"

    // Linting Support
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    application
}

group = "me.tet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Logging Support
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("org.slf4j:slf4j-simple:1.7.36")

    // Persistence | XML and JSON
    implementation("com.thoughtworks.xstream:xstream:1.4.19")
    implementation("org.codehaus.jettison:jettison:1.4.1")

    // Dokka Gradle plugin
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "MainKt"
    // for building a fat jar - include all dependencies
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}