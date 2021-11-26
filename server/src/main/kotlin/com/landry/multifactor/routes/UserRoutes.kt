package com.landry.multifactor.routes

import com.landry.multifactor.documentation.getUserByEmailDocs
import com.landry.multifactor.documentation.loginDocs
import com.landry.multifactor.documentation.registrationDocs
import com.landry.multifactor.exceptions.AuthenticationException
import com.landry.multifactor.notarizedPostRoute
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.repos.UserRepository
import com.landry.multifactor.responses.toUserResponse
import io.bkbn.kompendium.Notarized.notarizedGet
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    authenticate("jwt") {
        route("/users") {
            val usersRepo by inject<UserRepository>()

            baseUserRoutes(usersRepo)
        }
    }
}

private fun Route.baseUserRoutes(usersRepo: UserRepository) {
    notarizedGet(getUserByEmailDocs) {
        val email = call.parameters["email"] ?: throw IllegalArgumentException("email must not be null")
        val userResponse = usersRepo.getUserByEmail(email)!!.decrypt().toUserResponse()
        call.respond(status = HttpStatusCode.OK, userResponse)
    }
}