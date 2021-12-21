package com.landry.multifactor.datasource

import com.landry.multifactor.models.User
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.responses.toUserResponse

class MockUserDataSource: AbstractUsersDataSource {
    private val users = arrayListOf<User>()
    override suspend fun getUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun getUserById(id: String): UserResponse? {
        return users.firstOrNull { it.id == id }?.toUserResponse()
    }

    override suspend fun registerUser(user: User): UserResponse {
        users.add(user.copy(id = users.size.toString()))
        return users.last().toUserResponse()
    }

    override suspend fun userExists(email: String): Boolean {
        return getUserByEmail(email) != null
    }

    fun getRawUsersList(): List<User> {
        return users
    }
}
