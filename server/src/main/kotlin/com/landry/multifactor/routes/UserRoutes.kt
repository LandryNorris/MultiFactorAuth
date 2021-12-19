package com.landry.multifactor.routes

import com.landry.multifactor.documentation.getUserByEmailDocs
import com.landry.multifactor.repos.UserRepository
import com.landry.multifactor.responses.toUserResponse
import io.bkbn.kompendium.Notarized.notarizedGet
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
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
