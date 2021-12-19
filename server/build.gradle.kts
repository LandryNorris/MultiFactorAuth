val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometheus_version: String by project
val koin_version: String by project
val docsVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("com.google.cloud.tools.appengine") version "2.4.2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
}

val baseJarName = "MultiFactorServer"
group = "com.landry.multifactor"
version = "0.0.1"

application {
    mainClass.set("com.landry.multifactor.ApplicationKt")
}

appengine {
    deploy {
        version = "1"
        projectId = "skilled-curve-329023"
        stopPreviousVersion = true
    }
    val jarFile = File("build/libs/$baseJarName-$version-all.jar")
    stage.setArtifact(jarFile)
}

tasks {
    shadowJar {
        archiveBaseName.set(baseJarName)
        mergeServiceFiles()
        manifest {
            attributes["Main-Class"] = "com.landry.multifactor.ApplicationKt"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    appengineStage {
        dependsOn(shadowJar)
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("firebase-*-jvm-*.jar"))))
    implementation("io.bkbn:kompendium-core:$docsVersion")
    implementation("io.bkbn:kompendium-auth:$docsVersion")
    implementation("io.bkbn:kompendium-swagger-ui:$docsVersion")
    implementation("de.mkammerer:argon2-jvm:2.11")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-webjars:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
}