package com.landry.multifactor.responses

import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(val user: UserResponse, val token: String, val refreshToken: String)
