package com.landry.multifactor.params

import kotlinx.serialization.Serializable

@Serializable
data class DeviceParams(val mac: String, val userId: String, val deviceName: String)
