package com.landry.shared.http.responses

@Serializable
class LoginResponse(val email: String, val token: String)