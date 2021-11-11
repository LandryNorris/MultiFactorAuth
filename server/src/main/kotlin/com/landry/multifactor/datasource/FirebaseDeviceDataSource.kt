package com.landry.multifactor.datasource

import com.landry.multifactor.await
import com.landry.multifactor.firebaseApp
import com.landry.multifactor.models.Device
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseDeviceDataSource: AbstractDeviceDataSource() {
    private val firestore by lazy { Firebase.firestore(firebaseApp) }
    private val devices by lazy { firestore.collection("devices") }

    override suspend fun registerDevice(device: Device) {
        devices.add(device, Device.serializer())
    }

    override suspend fun getDeviceById(id: String): Device {
        return devices.document(id).await(Device.serializer())
    }

    override suspend fun getDevicesByUser(userId: String): List<Device> {
        return devices.where("userId", equalTo = userId).await(Device.serializer())
    }
}