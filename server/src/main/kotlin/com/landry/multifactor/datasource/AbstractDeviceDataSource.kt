package com.landry.multifactor.datasource

import com.landry.multifactor.models.Device

abstract class AbstractDeviceDataSource {
    abstract suspend fun registerDevice(device: Device)
    abstract suspend fun getDeviceById(id: String): Device
    abstract suspend fun getDevicesByUser(userId: String): List<Device>
}