package com.landry.shared.http.params

import kotlinx.serialization.Serializable

@Serializable
data class QueryDeviceParams(val userId: String?, val mac: String?, val name: String?, val isActive: Boolean?)