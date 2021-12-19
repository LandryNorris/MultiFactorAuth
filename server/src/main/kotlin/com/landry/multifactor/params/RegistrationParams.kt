package com.landry.multifactor.params

import kotlinx.serialization.Serializable

@Serializable
class RegistrationParams(val email: String, val firstName: String, val lastName: String, val password: String)
