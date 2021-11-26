package com.landry.multifactor.plugins

import com.landry.multifactor.documentation.*
import com.landry.multifactor.exceptions.*
import io.bkbn.kompendium.Notarized.notarizedException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import java.lang.IllegalArgumentException

fun Application.configureStatusPages() {
    install(StatusPages) {
        notarizedException<AuthenticationException, Unit>(authenticationExceptionDocs) {
            call.respond(HttpStatusCode.Unauthorized)
        }
        notarizedException<AuthorizationException, Unit>(authorizationExceptionDocs) {
            call.respond(HttpStatusCode.Forbidden)
        }
        notarizedException<EmailAlreadyExistsException, Unit>(emailExistsExceptionDocs) { cause ->
            call.respondText("Account already exists for ${cause.email}", status = HttpStatusCode.Conflict)
        }
        notarizedException<NullPointerException, Unit>(notFoundExceptionDocs) { cause ->
            call.respondText(notFoundExceptionDocs.description, status = HttpStatusCode.NotFound)
        }
        notarizedException<IllegalArgumentException, Unit>(illegalArgumentDocs) { cause ->
            call.respondText(cause.message ?: illegalArgumentDocs.description, status = HttpStatusCode.BadRequest)
        }
    }
}