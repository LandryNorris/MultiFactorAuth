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
    fun encrypt(key: SecretKey = EncryptionHelper.encryptionKeyStrong): User {
        val encryptedFirst = EncryptionHelper.encrypt(firstName.toByteArray(), key, iv.toByteArray()).base64Encode()
        val encryptedLast = EncryptionHelper.encrypt(lastName.toByteArray(), key, iv.toByteArray()).base64Encode()
        val encryptedHash = EncryptionHelper.encrypt(passwordHash.toByteArray(), key, iv.toByteArray()).base64Encode()
        return User(id, email, encryptedFirst, encryptedLast, encryptedHash, iv, isActive, isVerified)
    }

    fun decrypt(key: SecretKey = EncryptionHelper.encryptionKeyStrong): User {
        val decryptedFirst = EncryptionHelper.decrypt(firstName.base64Decode(), key, iv.toByteArray())
        val decryptedLast = EncryptionHelper.decrypt(lastName.base64Decode(), key, iv.toByteArray())
        val decryptedPasswordHash = EncryptionHelper.decrypt(passwordHash.base64Decode(), key, iv.toByteArray())
        return User(id, email, decryptedFirst, decryptedLast, decryptedPasswordHash, iv, isActive, isVerified)
    }

    fun decryptPasswordHash(): String {
        return EncryptionHelper.decryptStrong(passwordHash.base64Decode(), iv.toByteArray())
    }
}
