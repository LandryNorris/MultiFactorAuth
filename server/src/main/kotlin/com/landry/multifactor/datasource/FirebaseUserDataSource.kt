package com.landry.multifactor.datasource

import com.landry.multifactor.await
import com.landry.multifactor.firebaseApp
import com.landry.multifactor.models.User
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.responses.toUserResponse
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseUserDataSource: AbstractUsersDataSource() {
    private val database by lazy { Firebase.firestore(firebaseApp) }

    private val usersReference by lazy { database.collection("users") }

    override suspend fun getUserByEmail(email: String): User? {
        return usersReference.where("email", equalTo = email).await(User.serializer()).firstOrNull()
    }

    override suspend fun getUserById(id: String): UserResponse? {
        val user = usersReference.document(id).await(User.serializer())?.decrypt()
        return user?.toUserResponse()
    }

    override suspend fun registerUser(user: User): UserResponse {
        val createdUser = usersReference.add(User.serializer(), user).await(User.serializer())?.decrypt()!!
        return createdUser.toUserResponse()
    }

    override suspend fun userExists(email: String): Boolean {
        return usersReference.where("email", equalTo = email).get().documents.isNotEmpty()
    }
}