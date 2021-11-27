package com.landry.shared.http.params

import kotlinx.serialization.Serializable

@Serializable
data class RefreshParams(val refreshToken: String, val email: String)