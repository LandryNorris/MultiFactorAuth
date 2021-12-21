package com.landry.multifactor.datasource

import com.landry.multifactor.models.User

class MockUserDataSource: AbstractUsersDataSource {
    private val users = arrayListOf<User>()
    override suspend fun getUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun getUserById(id: String): User? {
        return users.firstOrNull { it.id == id }
    }

    override suspend fun registerUser(user: User): User {
        users.add(user.copy(id = users.size.toString()))
        return users.last()
    }

    override suspend fun userExists(email: String): Boolean {
        return getUserByEmail(email) != null
    }

    fun getRawUsersList(): List<User> {
        return users
    }
}
