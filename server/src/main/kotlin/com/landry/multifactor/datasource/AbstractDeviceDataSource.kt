package com.landry.multifactor.datasource

import com.landry.multifactor.models.Device
import com.landry.multifactor.responses.DeviceResponse
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams

interface AbstractDeviceDataSource {
    suspend fun registerDevice(device: Device): DeviceResponse?
    suspend fun getDeviceById(id: String): Device?
    suspend fun getDevicesByUser(userId: String): List<Device>
    suspend fun updateDevice(id: String, device: Device)
    suspend fun queryDevices(params: QueryDeviceParams): List<Device>
}
