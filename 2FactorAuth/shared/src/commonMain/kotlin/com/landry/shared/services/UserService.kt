package com.landry.shared.services

import com.landry.shared.http.Client
import com.landry.shared.http.responses.UserResponse
import com.landry.shared.models.User

class UserService {
    private val client = Client()

    suspend fun getUserByEmail(email: String): User {
        val userResponse: UserResponse = client.getJson("/users?email=$email")
        return userResponse.toUser()
    }
}