val entryPoint = "org.example.MainKt"

group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.1.10"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.github.ajalt.mordant:mordant:3.0.2")
    implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.2")
}

tasks.jar {
    manifest.attributes["Main-Class"] = entryPoint
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test { useJUnitPlatform() }
kotlin { jvmToolchain(21) }

graalvmNative {
    binaries {
        named("main") {
            mainClass.set(entryPoint)

            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(21))
                }
            )
        }
    }
}
