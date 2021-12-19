package com.landry.multifactor.datasource

import com.landry.multifactor.models.Device
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.responses.DeviceResponse
import com.landry.multifactor.responses.toResponse

class MockDeviceDataSource: AbstractDeviceDataSource {
    private var devices = mutableListOf<Device>()

    override suspend fun registerDevice(device: Device): DeviceResponse? {
        devices.add(device.copy(id = devices.size.toString()))
        return devices.last().toResponse()
    }

    override suspend fun getDeviceById(id: String): Device? {
        return devices.firstOrNull { it.id == id }
    }

    override suspend fun getDevicesByUser(userId: String): List<Device> {
        return devices.filter { it.userId == userId }
    }

    override suspend fun updateDevice(id: String, device: Device) {
        devices = devices.map { if(it.id == id) device else it }.toMutableList()
    }

    override suspend fun queryDevices(params: QueryDeviceParams): List<Device> {
        return devices.filter { device ->
            params.userId?.run {
                device.userId == this
            } ?: true && params.name?.run {
                device.deviceName == this
            } ?: true && params.mac?.run {
                device.mac == this
            } ?: true && params.isActive?.run {
                device.isActive == this
            } ?: true
        }
    }
}
