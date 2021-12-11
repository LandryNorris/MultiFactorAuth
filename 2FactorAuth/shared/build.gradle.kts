import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val ktorVersion: String by project
val jsonVersion: String by project
val coroutinesVersion: String by project
val decomposeVersion: String by project
val connectivityStatusVersion: String by project
val logVersion: String by project
val kryptoVersion: String by project
val fileVersion: String by project
val sqlVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-parcelize") // Apply the plugin for Android
    kotlin("plugin.serialization") version "1.5.31"
    id("com.squareup.sqldelight")
}

group = "me.landry"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$jsonVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("org.lighthousegames:logging:$logVersion")
                implementation("com.soywiz.korlibs.krypto:krypto:$kryptoVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlVersion")
                implementation("com.google.android.material:material:1.4.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)

sqldelight {
    database("OtpDatabase") {
        packageName = "com.landry.multifactor"
    }
}