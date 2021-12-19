package com.landry.multifactor.models

import com.landry.multifactor.base64Decode
import com.landry.multifactor.base64Encode
import com.landry.multifactor.utils.EncryptionHelper
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import javax.crypto.SecretKey

@Serializable
data class Device(
    @Transient override var id: String = "",
    val mac: String,
    val userId: String,
    val deviceName: String,
    val secret: String,
    val iv: String,
    val isActive: Boolean): IdAble {

    // We can't encrypt values we will be querying for in Firestore.
    // We skip encrypting mac and userID in case we want to query those fields.
    fun encrypt(key: SecretKey = EncryptionHelper.encryptionKeyStrong): Device {
        val encryptedDeviceName = EncryptionHelper
            .encrypt(deviceName.toByteArray(), key, iv.toByteArray())
            .base64Encode()
        val encryptedSecret = EncryptionHelper
            .encrypt(secret.toByteArray(), key, iv.toByteArray())
            .base64Encode()

        return Device(id, mac, userId, encryptedDeviceName, encryptedSecret, iv, isActive)
    }

    fun decrypt(key: SecretKey = EncryptionHelper.encryptionKeyStrong): Device {
        val decryptedDeviceName = EncryptionHelper
            .decrypt(deviceName.base64Decode(), key, iv.toByteArray())
        val decryptedSecret = EncryptionHelper
            .decrypt(secret.base64Decode(), key, iv.toByteArray())

        return Device(id, mac, userId, decryptedDeviceName, decryptedSecret, iv, isActive)
    }
}
