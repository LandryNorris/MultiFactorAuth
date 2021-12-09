package com.landry.shared.services

import com.landry.shared.http.Client
import com.landry.shared.http.params.RegistrationParams
import com.landry.shared.http.responses.UserResponse
import com.landry.shared.models.User

class RegistrationService {
    private val client = Client()

    suspend fun registerUser(email: String, firstName: String, lastName: String, password: String): User {
        val params = RegistrationParams(email, firstName, lastName, password)
        val userResponse: UserResponse = client.postJson("/register", params)
        return userResponse.toUser()
    }
}