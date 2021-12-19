val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val metricsVersion: String by project
val koinVersion: String by project
val docsVersion: String by project
val argonVersion: String by project
val fakerVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("com.google.cloud.tools.appengine") version "2.4.2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jetbrains.kotlinx.kover") version "0.4.4"
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

    test {
        kover {
            configure<kotlinx.kover.api.KoverTaskExtension> {
                coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ)
                includes = listOf("com\\.landry\\.multifactor\\..*")
            }
        }
    }

    build {
        dependsOn(shadowJar)
    }

    appengineStage {
        dependsOn(shadowJar)
    }

    appengineDeploy {
        dependsOn(koverReport)
    }

    koverHtmlReport {
        isEnabled = true
        htmlReportDir.set(layout.buildDirectory.dir("coverage/html"))
    }

    koverXmlReport {
        isEnabled = false
    }

    koverCollectReports {
        outputDir.set(layout.buildDirectory.dir("coverage") )
    }

    val verify by creating {
        dependsOn(detekt)
        dependsOn(koverReport)
    }

    val deploy by creating {
        dependsOn(verify)
        dependsOn(appengineDeploy)
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
    implementation("de.mkammerer:argon2-jvm:$argonVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-webjars:$ktorVersion")
    implementation("io.ktor:ktor-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$metricsVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.github.serpro69:kotlin-faker:$fakerVersion")
}
