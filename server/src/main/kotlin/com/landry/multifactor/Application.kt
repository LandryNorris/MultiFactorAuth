package com.landry.multifactor

import io.ktor.application.*
import com.landry.multifactor.plugins.*
import com.typesafe.config.ConfigFactory
import dev.gitlive.firebase.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused") // application.example.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    ConfigFactory.load("application.conf")
    configureDocumentation()
    configureKoin()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureAdministration()
    configureRouting()
    configureStatusPages()
}
