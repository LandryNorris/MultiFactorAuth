package com.landry.multifactor.routes

import com.landry.multifactor.documentation.loginDocs
import com.landry.multifactor.documentation.registrationDocs
import com.landry.multifactor.exceptions.AuthenticationException
import com.landry.multifactor.notarizedPostRoute
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.repos.UserRepository
import io.ktor.application.*
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
            call.respond(io.ktor.http.HttpStatusCode.OK, loginResponse)
        }
    }

    notarizedPostRoute("/register", registrationDocs) {
        println("registering user")
        val registrationParams = call.receive<RegistrationParams>()
        val registrationResponse = usersRepo.register(registrationParams)
        call.respond(io.ktor.http.HttpStatusCode.OK, registrationResponse)
    }
}