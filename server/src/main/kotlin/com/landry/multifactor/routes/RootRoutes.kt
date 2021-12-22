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
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import org.koin.ktor.ext.inject

fun Route.rootRoutes() {

    notarizedGetRoute("/", rootDocs) {
        call.respondText("MultiFactorAPI")
    }
}
