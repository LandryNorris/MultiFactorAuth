package com.landry.multifactor

import com.landry.multifactor.models.Device
import com.landry.multifactor.models.User
import com.landry.multifactor.utils.EncryptionHelper
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EncryptionTest {

    val encryptionHelper = EncryptionHelper()

    @Test
    fun testGenerateKey() {
        val key = encryptionHelper.generateKey().encoded.base64Encode()
        assertTrue(key.isNotEmpty())
    }

    @Test
    fun testKeyStorage() {
        val key = encryptionHelper.generateKey().encoded
        val keyText = key.base64Encode()
        val decodedKey = keyText.base64Decode()
        assertContentEquals(key, decodedKey)
    }

    @Test
    fun testEncryptDecryptString() {
        val key = encryptionHelper.generateKey()
        val iv = encryptionHelper.generateIV().decodeToString()

        val message = "My message"

        val encrypted = encryptionHelper.encrypt(message.toByteArray(), key, iv.toByteArray())
        val encryptedString = encrypted.base64Encode()
        assertContentEquals(encrypted, encryptedString.base64Decode())
        val decrypted = encryptionHelper.decrypt(encryptedString.base64Decode(), key, iv.toByteArray())

        assertEquals(message, decrypted)
    }

    @Test
    fun testEncryptingDecryptingUser() {
        val key = encryptionHelper.generateKey()
        val iv = encryptionHelper.generateIV().base64Encode()
        val user = User(
            "",
            "email@email.test",
            "Landry",
            "Norris",
            "A Password",
            iv, isActive = false, isVerified = false)

        val encryptedUser = user.encrypt(key)
        assertEquals(user.iv, encryptedUser.iv)
        assertEquals(user, encryptedUser.decrypt(key))
    }

    @Test
    fun testEncryptingDecryptingDevice() {
        val key = encryptionHelper.generateKey()
        val iv = encryptionHelper.generateIV().base64Encode()
        val device = Device("", "A MAC value", "A user ID", "A Device Name", "A Secret Value", iv, true)

        val encryptedDevice = device.encrypt(key)
        assertEquals(device.iv, encryptedDevice.iv)
        assertEquals(device, encryptedDevice.decrypt(key))
    }
}
