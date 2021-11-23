package com.landry.multifactor.datasource

import com.landry.multifactor.await
import com.landry.multifactor.firebaseApp
import com.landry.multifactor.models.User
import com.landry.multifactor.models.UserResponse
import com.landry.multifactor.models.toUserResponse
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseUserDataSource: AbstractUsersDataSource() {
    private val database by lazy { Firebase.firestore(firebaseApp) }

    private val usersReference by lazy { database.collection("users") }

    companion object {
        const val MAX_HASH_TIME = 1000L
        const val HASH_MEMORY = 1024
        const val HASH_PARALLELISM = 2
    }

    override suspend fun getUserByEmail(email: String): User? = usersReference.where("email", equalTo = email).await(User.serializer()).firstOrNull()

    override suspend fun getUserById(id: String): UserResponse? {
        val user = usersReference.document(id).await(User.serializer())
        return user?.toUserResponse()
    }

    override suspend fun registerUser(email: String, firstName: String, lastName: String, hashedPassword: String): UserResponse {
        val user = User("", email, firstName, lastName, hashedPassword)

        val createdUser = usersReference.add(User.serializer(), user).await(User.serializer())!!
        return createdUser.toUserResponse()
    }
}