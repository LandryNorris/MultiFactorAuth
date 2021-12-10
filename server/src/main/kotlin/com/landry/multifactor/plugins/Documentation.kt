package com.landry.multifactor.plugins

import com.landry.multifactor.documentation.swaggerDocs
import com.landry.multifactor.notarizedGetRoute
import io.bkbn.kompendium.Kompendium
import io.bkbn.kompendium.models.oas.OpenApiSpecInfo
import io.bkbn.kompendium.models.oas.OpenApiSpecServer
import io.bkbn.kompendium.routes.openApi
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.net.URI

fun Application.configureDocumentation() {
    routing {
        openApi(openApiSpec)

        notarizedGetRoute("/swagger-ui", swaggerDocs) {
            val openApiJsonUrl = "/openapi.json"
            call.respondRedirect("/webjars/swagger-ui/index.html?url=$openApiJsonUrl", true)
        }
    }
}

val openApiSpec = Kompendium.openApiSpec.copy(
    info = OpenApiSpecInfo(
        title = "Multifactor",
        version = "0.0.1",
        description = "An API for multifactor authentication"
    ),
    servers = mutableListOf(
        OpenApiSpecServer(URI("http://localhost:8080")),
        OpenApiSpecServer(URI("https://skilled-curve-329023.uc.r.appspot.com/"))
    )
)