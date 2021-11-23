package com.landry.multifactor.params

import kotlinx.serialization.Serializable

@Serializable
class LoginParams(val email: String, val password: String)