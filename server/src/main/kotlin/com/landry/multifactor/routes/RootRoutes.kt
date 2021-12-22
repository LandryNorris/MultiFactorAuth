package com.landry.multifactor.routes

import com.landry.multifactor.documentation.rootDocs
import com.landry.multifactor.notarizedGetRoute
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Route

fun Route.rootRoutes() {
    notarizedGetRoute("/", rootDocs) {
        call.respondText("MultiFactorAPI")
    }
}
