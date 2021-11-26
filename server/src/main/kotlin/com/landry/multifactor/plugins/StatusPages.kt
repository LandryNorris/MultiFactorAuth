package com.landry.multifactor.plugins

import com.landry.multifactor.documentation.authenticationExceptionDocs
import com.landry.multifactor.documentation.authorizationExceptionDocs
import com.landry.multifactor.documentation.emailExistsExceptionDocs
import com.landry.multifactor.documentation.notFoundExceptionDocs
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import io.bkbn.kompendium.Notarized.notarizedException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

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
            call.respondText("Not Found ${cause.message}", status = HttpStatusCode.NotFound)
        }
    }
}