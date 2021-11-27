package com.landry.shared.http.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val email: String, val token: String, val refreshToken: String)