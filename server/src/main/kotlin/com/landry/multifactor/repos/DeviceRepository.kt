package com.landry.multifactor.repos

import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.models.Device
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.responses.CreateDeviceResponse
import com.landry.multifactor.utils.EncryptionHelper
import com.landry.multifactor.utils.SecretGenerator

class DeviceRepository(private val dataSource: AbstractDeviceDataSource) {
    suspend fun registerDevice(params: DeviceParams): CreateDeviceResponse? {
        val secret = SecretGenerator.generate()
        val iv = EncryptionHelper.generateIV().decodeToString()
        val device = Device("", params.mac, params.userId, params.deviceName, secret, iv, true)
        val deviceResponse = dataSource.registerDevice(device) ?: return null
        return CreateDeviceResponse(deviceResponse, secret)
    }
    suspend fun getDevice(id: String) = dataSource.getDeviceById(id)
}