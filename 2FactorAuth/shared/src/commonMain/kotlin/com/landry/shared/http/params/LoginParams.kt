package com.landry.shared.http.params

import kotlinx.serialization.Serializable

@Serializable
class LoginParams(val email: String, val password: String)