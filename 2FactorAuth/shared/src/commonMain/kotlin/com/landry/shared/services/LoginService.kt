package com.landry.shared.services

import com.landry.shared.http.Client
import com.landry.shared.http.params.LoginParams
import com.landry.shared.http.responses.LoginResponse

class LoginService {
    private val client = Client()

    suspend fun performLogin(email: String, password: String): LoginResponse {
        val params = LoginParams(email, password)
        return client.postJson("/login", params)
    }
}