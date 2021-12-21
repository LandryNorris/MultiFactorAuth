package com.landry.multifactor.datasource

import com.landry.multifactor.await
import com.landry.multifactor.firebaseApp
import com.landry.multifactor.models.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseUserDataSource: AbstractUsersDataSource {
    private val database by lazy { Firebase.firestore(firebaseApp) }

    private val usersReference by lazy { database.collection("users") }

    override suspend fun getUserByEmail(email: String): User? {
        return usersReference.where("email", equalTo = email).await(User.serializer()).firstOrNull()
    }

    override suspend fun getUserById(id: String): User? {
        return usersReference.document(id).await(User.serializer())
    }

    override suspend fun registerUser(user: User): User {
        return usersReference.add(User.serializer(), user).await(User.serializer())!!
    }

    override suspend fun userExists(email: String): Boolean {
        return usersReference.where("email", equalTo = email).get().documents.isNotEmpty()
    }
}
