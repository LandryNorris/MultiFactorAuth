package com.landry.multifactor.responses

import kotlinx.serialization.Serializable

@Serializable
class RefreshResponse(val token: String, val refreshToken: String)
