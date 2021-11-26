package com.landry.multifactor

import com.google.cloud.firestore.Blob
import com.landry.multifactor.models.User
import com.landry.multifactor.utils.EncryptionHelper
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EncryptionTest {

    @Test
    fun testGenerateKey() {
        val key = EncryptionHelper.generateKey().encoded.base64Encode()
        assertTrue(key.isNotEmpty())
    }

    @Test
    fun testKeyStorage() {
        val key = EncryptionHelper.generateKey().encoded
        val keyText = key.base64Encode()
        val decodedKey = keyText.base64Decode()
        assertContentEquals(key, decodedKey)
    }

    @Test
    fun testEncryptDecryptString() {
        val key = EncryptionHelper.generateKey()
        val iv = EncryptionHelper.generateIV().decodeToString()

        val message = "My message"

        val encrypted = EncryptionHelper.encrypt(message.toByteArray(), key, iv.toByteArray())
        val encryptedString = encrypted.base64Encode()
        assertContentEquals(encrypted, encryptedString.base64Decode())
        val decrypted = EncryptionHelper.decrypt(encryptedString.base64Decode(), key, iv.toByteArray())

        assertEquals(message, decrypted)
    }

    @Test
    fun testEncryptingDecryptingUser() {
        val key = EncryptionHelper.generateKey()
        val iv = EncryptionHelper.generateIV().base64Encode()
        val user = User("", "email@email.test", "Landry", "Norris", "A Password", iv)

        val encryptedUser = user.encrypt(key)
        assertEquals(user.iv, encryptedUser.iv)
        assertEquals(user, encryptedUser.decrypt(key))
    }
}