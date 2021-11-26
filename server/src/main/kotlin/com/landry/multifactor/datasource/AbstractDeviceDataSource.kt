package com.landry.multifactor.datasource

import com.landry.multifactor.models.Device
import com.landry.multifactor.responses.DeviceResponse
import com.landry.multifactor.params.DeviceParams

abstract class AbstractDeviceDataSource {
    abstract suspend fun registerDevice(device: Device): DeviceResponse?
    abstract suspend fun getDeviceById(id: String): Device?
    abstract suspend fun getDevicesByUser(userId: String): List<Device>
}