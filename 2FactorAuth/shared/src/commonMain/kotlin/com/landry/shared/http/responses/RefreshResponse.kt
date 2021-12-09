package com.landry.shared.http.responses

import kotlinx.serialization.Serializable

@Serializable
class RefreshResponse(val token: String, val refreshToken: String)