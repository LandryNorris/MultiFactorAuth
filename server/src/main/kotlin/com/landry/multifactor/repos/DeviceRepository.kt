package com.landry.multifactor.repos

import com.landry.multifactor.base64Encode
import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.models.Device
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.responses.CreateDeviceResponse
import com.landry.multifactor.responses.toResponse
import com.landry.multifactor.utils.EncryptionHelper
import com.landry.multifactor.utils.SecretGenerator

class DeviceRepository(private val dataSource: AbstractDeviceDataSource) {
    suspend fun registerDevice(params: DeviceParams): CreateDeviceResponse? {
        val secret = SecretGenerator.generate()
        val iv = EncryptionHelper.generateIV().base64Encode()
        val device = Device("", params.mac, params.userId, params.deviceName, secret, iv, true)
        val deviceResponse = dataSource.registerDevice(device.encrypt()) ?: return null
        device.id = deviceResponse.deviceId
        return CreateDeviceResponse(device.toResponse(), secret)
    }
    suspend fun getDevice(id: String) = dataSource.getDeviceById(id)

    suspend fun queryDevices(params: QueryDeviceParams) = dataSource.queryDevices(params)

    /**
     * Deactivate a given device. Throws a NullPointerException if the device does not exist.
     */
    @Throws(NullPointerException::class)
    suspend fun deactivateDevice(id: String) {
        val existingDevice = getDevice(id)!!
        val newDevice = existingDevice.copy(isActive = false)
        dataSource.updateDevice(id, newDevice)
    }
}