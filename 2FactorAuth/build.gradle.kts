

buildscript {
    val composeVersion: String by project

    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
        //classpath("com.android.tools.lint:lint:30.0.4")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.3")
    }
}

group = "me.landry"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}