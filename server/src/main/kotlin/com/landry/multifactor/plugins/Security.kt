package com.landry.multifactor.plugins

import io.ktor.auth.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.bkbn.kompendium.auth.KompendiumAuth.notarizedJwt
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureSecurity() {
    authentication {
        notarizedJwt("jwt") {
            val jwtAudience = environment.config.property("jwt.audience").getString()
            val domain = environment.config.property("jwt.domain").getString()
            val secret = environment.config.property("jwt.secret").getString()
            realm = environment.config.property("jwt.realm").getString()
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(jwtAudience)
                    .withIssuer(domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
