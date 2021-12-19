package com.landry.multifactor.datasource

import com.landry.multifactor.models.User
import com.landry.multifactor.responses.UserResponse

interface AbstractUsersDataSource {
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(id: String): UserResponse?
    suspend fun registerUser(user: User): UserResponse
    suspend fun userExists(email: String): Boolean
}
