import org.jetbrains.compose.compose

val decomposeVersion: String by project

plugins {
    id("com.android.application")
    kotlin("android")
    //id("org.jetbrains.compose")
}

group = "me.landry"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.5.0")
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.preview)
    implementation("androidx.compose.ui:ui:1.0.5")

//    // Tooling support (Previews, etc.)
//    implementation("androidx.compose.ui:ui-tooling:1.0.5")
//    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
//    implementation("androidx.compose.foundation:foundation:1.0.5")
//    // Material Design
//    implementation("androidx.compose.material:material:1.0.5")
//    // Material design icons
//    implementation("androidx.compose.material:material-icons-core:1.0.5")
//    implementation("androidx.compose.material:material-icons-extended:1.0.5")
//    // Integration with observables
//    implementation("androidx.compose.runtime:runtime-livedata:1.0.5")
//    implementation("androidx.compose.runtime:runtime-rxjava2:1.0.5")

    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.0.5")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.landry.multifactor"
        minSdk = 24
        targetSdk = 31
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-rc03"
    }
}