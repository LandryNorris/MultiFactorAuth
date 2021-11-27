package com.landry.multifactor.responses

import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(val email: String, val token: String, val refreshToken: String)