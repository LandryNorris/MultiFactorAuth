package com.landry.multifactor

import io.ktor.application.*
import com.landry.multifactor.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
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
