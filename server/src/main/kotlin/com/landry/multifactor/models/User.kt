package com.landry.multifactor.models

import com.landry.multifactor.base64Decode
import com.landry.multifactor.base64Encode
import com.landry.multifactor.utils.EncryptionHelper
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import javax.crypto.SecretKey

@Serializable
data class User(@Transient override var id: String = "", var email: String, var firstName: String,
                var lastName: String, val passwordHash: String, val iv: String,
                val isActive: Boolean, val isVerified: Boolean): IdAble {
    @Transient private val encryptionHelper = EncryptionHelper()

    fun encrypt(key: SecretKey): User {
        val encryptedFirst = encryptionHelper.encrypt(firstName.toByteArray(), key, iv.toByteArray()).base64Encode()
        val encryptedLast = encryptionHelper.encrypt(lastName.toByteArray(), key, iv.toByteArray()).base64Encode()
        val encryptedHash = encryptionHelper.encrypt(passwordHash.toByteArray(), key, iv.toByteArray()).base64Encode()
        return User(id, email, encryptedFirst, encryptedLast, encryptedHash, iv, isActive, isVerified)
    }

    fun decrypt(key: SecretKey): User {
        val decryptedFirst = encryptionHelper.decrypt(firstName.base64Decode(), key, iv.toByteArray())
        val decryptedLast = encryptionHelper.decrypt(lastName.base64Decode(), key, iv.toByteArray())
        val decryptedPasswordHash = encryptionHelper.decrypt(passwordHash.base64Decode(), key, iv.toByteArray())
        return User(id, email, decryptedFirst, decryptedLast, decryptedPasswordHash, iv, isActive, isVerified)
    }
}
