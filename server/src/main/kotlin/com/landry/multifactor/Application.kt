package com.landry.multifactor

import io.ktor.application.*
import com.landry.multifactor.plugins.*
import dev.gitlive.firebase.*

val firebaseApp by lazy { Firebase.initialize(Unit, AdminFirebaseOptions("skilled-curve-329023-firebase-adminsdk-wkpj4-a0ad15583c.json")) }

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
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
