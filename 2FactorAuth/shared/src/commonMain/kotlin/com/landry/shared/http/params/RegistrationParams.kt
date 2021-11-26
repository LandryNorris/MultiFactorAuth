package com.landry.shared.http.params

@Serializable
class RegistrationParams(val email: String, val firstName: String, val lastName: String, val password: String)