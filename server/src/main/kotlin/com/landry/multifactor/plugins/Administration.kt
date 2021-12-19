package com.landry.multifactor.plugins

import io.ktor.server.engine.*
import io.ktor.application.*

fun Application.configureAdministration() {
    install(ShutDownUrl.ApplicationCallFeature) {
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }
}
