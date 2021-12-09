package com.landry.multifactor

import com.landry.multifactor.plugins.*
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.testing.*

fun Application.setup() {
    configureKoinTest()
    configureSecurity()
    configureDocumentation()
    configureHTTP()
    configureSerialization()
    configureAdministration()
    configureStatusPages()
    configureRouting()
}

fun <R> setupKtorTest(test: TestApplicationEngine.() -> R) {
    withTestApplication {
        (environment.config as MapApplicationConfig).apply {
            put("jwt.audience", "")
            put("jwt.secret", "")
            put("jwt.domain", "")
            put("jwt.realm", "")
        }
        application.setup()
        test()
    }
}