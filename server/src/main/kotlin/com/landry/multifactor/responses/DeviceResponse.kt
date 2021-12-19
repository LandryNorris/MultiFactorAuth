package com.landry.multifactor.responses

import com.landry.multifactor.models.Device
import kotlinx.serialization.Serializable

@Serializable
class DeviceResponse(
    val deviceId: String,
    val mac: String,
    val userId: String,
    val deviceName: String,
    val isActive: Boolean)

@Serializable
class CreateDeviceResponse(val device: DeviceResponse, val secret: String)

fun Device.toResponse() = run { DeviceResponse(id, mac, userId, deviceName, isActive) }
