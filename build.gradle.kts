
group = "org.example"
version = "1.0-SNAPSHOT"
val entryPoint = "$group.MainKt"

plugins {
    kotlin("jvm") version "2.1.10"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

kotlin { jvmToolchain(21) }
repositories { mavenCentral() }

dependencies {
    implementation("com.github.ajalt.mordant:mordant:3.0.2")
    implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.2")
    implementation("com.github.ajalt.clikt:clikt:5.0.3")

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }
tasks.jar {
    manifest.attributes["Main-Class"] = entryPoint
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from( configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }
    )
}

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
