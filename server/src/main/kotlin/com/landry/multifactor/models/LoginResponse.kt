package com.landry.multifactor.models

import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(val email: String, val token: String)