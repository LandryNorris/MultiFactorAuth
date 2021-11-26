package com.landry.shared.services

import com.landry.shared.http.Client
import com.landry.shared.http.responses.LoginResponse

class LoginService {
    val client = Client()

    suspend fun performLogin(email: String, password: String) {
        val loginResponse = client.postJson<LoginResponse>("/users/login")
    }
}