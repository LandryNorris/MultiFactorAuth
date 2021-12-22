package com.landry.multifactor.routes

import com.landry.multifactor.documentation.getUserByEmailDocs
import com.landry.multifactor.documentation.loginDocs
import com.landry.multifactor.documentation.refreshDocs
import com.landry.multifactor.documentation.registrationDocs
import com.landry.multifactor.exceptions.AuthenticationException
import com.landry.multifactor.notarizedPostRoute
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.repos.UserRepository
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.responses.toUserResponse
import io.bkbn.kompendium.Notarized.notarizedGet
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.userRoutes() {

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

        route("/users") {
            val usersRepo by inject<UserRepository>()
            baseUserRoutes(usersRepo)
        }
    }
}

private fun Route.baseUserRoutes(usersRepo: UserRepository) {
    notarizedGet(getUserByEmailDocs) {
        val email = call.parameters["email"] ?: throw IllegalArgumentException("email must not be null")
        val userResponse = usersRepo.getUserByEmail(email)!!.toUserResponse()
        call.respond(status = HttpStatusCode.OK, userResponse)
    }
}
