package com.landry.multifactor.responses

import com.landry.multifactor.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val id: String, val firstName: String, val lastName: String, val email: String)

fun User.toUserResponse(): UserResponse {
    return UserResponse(id, firstName, lastName, email)
}