val decomposeVersion: String by project

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = "me.landry"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.activity:activity-compose:1.4.0")
    //implementation("androidx.compose.ui:ui-tooling:1.0.5")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.landry.twofactor"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}