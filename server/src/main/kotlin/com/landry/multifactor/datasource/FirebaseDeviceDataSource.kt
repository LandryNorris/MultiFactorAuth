package com.landry.multifactor.datasource

import com.landry.multifactor.await
import com.landry.multifactor.firebaseApp
import com.landry.multifactor.models.Device
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.responses.DeviceResponse
import com.landry.multifactor.responses.toResponse
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Query
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseDeviceDataSource: AbstractDeviceDataSource {
    private val firestore by lazy { Firebase.firestore(firebaseApp) }
    private val devices by lazy { firestore.collection("devices") }

    override suspend fun registerDevice(device: Device): DeviceResponse? {
        val newDevice = devices.add(device, Device.serializer()).await(Device.serializer()) ?: return null

        return newDevice.toResponse()
    }

    override suspend fun getDeviceById(id: String): Device? {
        return devices.document(id).await(Device.serializer())
    }

    override suspend fun getDevicesByUser(userId: String): List<Device> {
        return devices.where("userId", equalTo = userId).await(Device.serializer())
    }

    override suspend fun updateDevice(id: String, device: Device) {
        devices.document(id).update(device)
    }

    override suspend fun queryDevices(params: QueryDeviceParams): List<Device> = params.run {
        return devices.whereIfNotNull("userId", userId)
            .whereIfNotNull("mac", mac)
            .whereIfNotNull("isActive", active).await(Device.serializer())
    }

    private fun Query.whereIfNotNull(field: String, equalTo: Any?): Query {
        return if(equalTo == null) this
        else where(field, equalTo = equalTo)
    }
}
