package com.landry.multifactor.plugins

import com.landry.multifactor.routes.deviceRoutes
import com.landry.multifactor.routes.userRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.webjars.*
import java.time.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {
    install(AutoHeadResponse)

    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }

    routing {
        userRoutes()
        deviceRoutes()
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
