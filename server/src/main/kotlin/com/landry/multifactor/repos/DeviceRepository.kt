package com.landry.multifactor.repos

import com.landry.multifactor.base64Encode
import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.encryptionKeyStrong
import com.landry.multifactor.models.Device
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.responses.CreateDeviceResponse
import com.landry.multifactor.responses.toResponse
import com.landry.multifactor.utils.EncryptionHelper
import com.landry.multifactor.utils.SecretGenerator
import io.ktor.config.ApplicationConfig

class DeviceRepository(private val dataSource: AbstractDeviceDataSource, val config: ApplicationConfig) {
    private val encryptionKey = config.encryptionKeyStrong()

    suspend fun registerDevice(params: DeviceParams): CreateDeviceResponse {
        val secret = SecretGenerator.generate()
        val iv = EncryptionHelper().generateIV().base64Encode()
        val device = Device("", params.mac, params.userId, params.deviceName, secret, iv, true)
        val deviceResponse = dataSource.registerDevice(device.encrypt(encryptionKey))
            .decrypt(encryptionKey).toResponse()

        device.id = deviceResponse.deviceId
        return CreateDeviceResponse(deviceResponse, secret)
    }

    suspend fun getDevice(id: String) = dataSource.getDeviceById(id)?.decrypt(encryptionKey)

    suspend fun queryDevices(params: QueryDeviceParams): List<Device> {
        params.run {
            if(listOf<Any?>(userId, mac, active).all { it == null })
                throw IllegalArgumentException("No parameters provided")
        }
        return dataSource.queryDevices(params).map { it.decrypt(encryptionKey) }
    }

    /**
     * Deactivate a given device. Throws a NullPointerException if the device does not exist.
     */
    @Throws(NullPointerException::class)
    suspend fun deactivateDevice(id: String) {
        val existingDevice = dataSource.getDeviceById(id)!!
        val newDevice = existingDevice.copy(isActive = false)
        dataSource.updateDevice(id, newDevice)
    }
}
