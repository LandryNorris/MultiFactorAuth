package com.landry.shared.services

import com.landry.shared.http.Client
import com.landry.shared.http.params.LoginParams
import com.landry.shared.http.responses.LoginResponse
import io.ktor.client.features.auth.providers.*

class LoginService {
    private val client = Client()

    suspend fun performLogin(email: String, password: String) {
        val params = LoginParams(email, password)
        val loginResponse: LoginResponse = client.postJson("/users/login", params)
        Client.bearerTokens = BearerTokens(loginResponse.token, loginResponse.refreshToken)
    }
}