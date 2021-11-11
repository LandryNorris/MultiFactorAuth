package com.landry.multifactor.repos

import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.models.Device

class DeviceRepository(val dataSource: AbstractDeviceDataSource) {
    suspend fun registerDevice(device: Device) = dataSource.registerDevice(device)
    suspend fun getDevice(id: String) = dataSource.getDeviceById(id)
}