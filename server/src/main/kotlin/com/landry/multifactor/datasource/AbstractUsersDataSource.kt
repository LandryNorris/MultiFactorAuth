package com.landry.multifactor.datasource

import com.landry.multifactor.models.User
import com.landry.multifactor.models.UserResponse

abstract class AbstractUsersDataSource {
    abstract suspend fun getUserByEmail(email: String): User?
    abstract suspend fun getUserById(id: String): UserResponse?
    abstract suspend fun registerUser(email: String, firstName: String, lastName: String, hashedPassword: String): UserResponse
}