package com.landry.multifactor.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.landry.multifactor.inject
import com.landry.multifactor.toDate
import io.ktor.config.*
import java.time.LocalDateTime

class TokenGenerator {
    companion object {
        fun generate(email: String): String {
            val config by inject<ApplicationConfig>()
            val jwtAudience = config.property("jwt.audience").getString()
            val domain = config.property("jwt.domain").getString()
            val secret = config.property("jwt.secret").getString()
            val durationHours = 2L
            val expiresAt = LocalDateTime.now().plusHours(durationHours).toDate()
            return JWT.create()
                .withClaim("email", email)
                .withAudience(jwtAudience)
                .withIssuer(domain)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secret))
        }
    }
}