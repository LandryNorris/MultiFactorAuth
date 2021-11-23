package com.landry.multifactor.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val id: String, val firstName: String, val lastName: String, val email: String)

fun User.toUserResponse(): UserResponse {
    return UserResponse(id, firstName, lastName, email)
}