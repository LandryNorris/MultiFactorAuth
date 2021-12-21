package com.landry.multifactor.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.landry.multifactor.config
import com.landry.multifactor.inject
import com.landry.multifactor.toDate
import io.ktor.config.*
import java.time.LocalDateTime
import java.time.ZoneOffset

class TokenGenerator private constructor() {
    companion object {
        fun generate(email: String): String {
            val secret = config.property("jwt.secret").getString()
            return buildJWT(email, durationHours = 1)
                .sign(Algorithm.HMAC256(secret))
        }

        fun generateRefresh(email: String): String {
            val secret = config.property("jwt.secret").getString()
            return buildJWT(email, durationMonths = 1)
                .withClaim("refresh", true)
                .sign(Algorithm.HMAC256(secret))
        }

        private fun buildJWT(email: String,
                             durationHours: Long = 0,
                             durationDays: Long = 0,
                             durationMonths: Long = 0): JWTCreator.Builder {
            val jwtAudience = config.property("jwt.audience").getString()
            val domain = config.property("jwt.domain").getString()
            val expiresAt = LocalDateTime.now(ZoneOffset.UTC)
                .plusHours(durationHours)
                .plusDays(durationDays)
                .plusMonths(durationMonths).toDate()

            return JWT.create()
                .withClaim("email", email)
                .withAudience(jwtAudience)
                .withIssuer(domain)
                .withExpiresAt(expiresAt)
        }
    }
}
