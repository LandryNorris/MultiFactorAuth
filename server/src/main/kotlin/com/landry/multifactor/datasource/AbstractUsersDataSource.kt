package com.landry.multifactor.datasource

import com.landry.multifactor.models.User
import com.landry.multifactor.responses.UserResponse

abstract class AbstractUsersDataSource {
    abstract suspend fun getUserByEmail(email: String): User?
    abstract suspend fun getUserById(id: String): UserResponse?
    abstract suspend fun registerUser(user: User): UserResponse
    abstract suspend fun userExists(email: String): Boolean
}