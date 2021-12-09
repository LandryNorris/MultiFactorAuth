package com.landry.multifactor.routes

import com.landry.multifactor.documentation.loginDocs
import com.landry.multifactor.documentation.refreshDocs
import com.landry.multifactor.documentation.registrationDocs
import com.landry.multifactor.documentation.rootDocs
import com.landry.multifactor.exceptions.AuthenticationException
import com.landry.multifactor.notarizedGetRoute
import com.landry.multifactor.notarizedPostRoute
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.repos.UserRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.rootRoutes() {
    val usersRepo by inject<UserRepository>()

    notarizedPostRoute("/login", loginDocs) {
        val loginParams = call.receive<LoginParams>()
        val loginResponse = usersRepo.login(loginParams.email, loginParams.password)

        if (loginResponse == null) {
            throw AuthenticationException()
        } else {
            call.respond(HttpStatusCode.OK, loginResponse)
        }
    }

    notarizedPostRoute("/register", registrationDocs) {
        val registrationParams = call.receive<RegistrationParams>()
        val registrationResponse = usersRepo.register(registrationParams)
        call.respond(HttpStatusCode.OK, registrationResponse)
    }

    authenticate("jwt") {
        notarizedPostRoute("/refresh", refreshDocs) {
            val params = call.receive<RefreshParams>()
            val response = usersRepo.refresh(params)

            call.respond(response)
        }
    }

    notarizedGetRoute("/", rootDocs) {
        call.respondText("MultiFactorAPI")
    }
}