package com.landry.shared.http.responses

import com.landry.shared.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val id: String, val firstName: String, val lastName: String, val email: String) {
    fun toUser(): User = run { User(id, firstName, lastName, email) }
}