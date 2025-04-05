plugins {
    kotlin("multiplatform") version "2.1.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    applyDefaultHierarchyTemplate()


    mingwX64("Windows") {
        binaries {
            executable {
                entryPoint = "org.example.main"
            }
        }
    }

//    linuxX64("Linux") {
//        binaries {
//            executable {
//                entryPoint = "org.example.main"
//            }
//        }
//    }

//    macosArm64("MacOS") {
//        binaries {
//            executable {
//                entryPoint = "org.example.main"
//            }
//        }
//    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("com.github.ajalt.mordant:mordant:3.0.2")
                implementation("com.github.ajalt.mordant:mordant-coroutines:3.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
            }
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.BIN
}