plugins {
    id("com.android.application")
    kotlin("android")
}

group = "me.landry"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    //implementation("androidx.appcompat:appcompat:1.4.0")
    //implementation("androidx.constraint:constraintlayout:2.0.2")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "me.landry.androidApp"
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