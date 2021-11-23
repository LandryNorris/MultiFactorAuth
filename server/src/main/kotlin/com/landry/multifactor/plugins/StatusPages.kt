package com.landry.multifactor.plugins

import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<EmailAlreadyExistsException> { cause ->
            call.respondText("Account already exists for ${cause.email}", status = HttpStatusCode.Conflict)
        }
    }
}