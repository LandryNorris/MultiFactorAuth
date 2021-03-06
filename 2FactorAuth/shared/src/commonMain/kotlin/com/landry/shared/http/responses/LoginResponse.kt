package com.landry.shared.http.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val user: UserResponse, val token: String, val refreshToken: String)