package com.landry.multifactor.utils

import com.landry.multifactor.base64Decode
import com.landry.multifactor.base64Encode
import com.landry.multifactor.inject
import com.landry.multifactor.koin
import io.ktor.config.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper private constructor() {
    companion object {
        private const val AES_KEY_SIZE = 256
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val KEY_ALOGRITHM = "AES"

        val encryptionKeyStrong by lazy {
            val config by koin.inject<ApplicationConfig>()
            val keyBytes = config.property("encryption.strongKey").getString().base64Decode()
            SecretKeySpec(keyBytes, "AES")
        }

        fun encryptStrong(plainText: ByteArray, iv: ByteArray) = encrypt(plainText, encryptionKeyStrong, iv)
        fun decryptStrong(plainText: ByteArray, iv: ByteArray) = decrypt(plainText, encryptionKeyStrong, iv)

        fun encrypt(plainText: ByteArray, key: SecretKey, iv: ByteArray,
                    algorithm: String = ALGORITHM, keyAlgorithm: String = KEY_ALOGRITHM): ByteArray {
            val cipher = Cipher.getInstance(algorithm)
            val keySpec = SecretKeySpec(key.encoded, keyAlgorithm)
            val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec)

            return cipher.doFinal(plainText)
        }

        fun decrypt(encrypted: ByteArray, key: SecretKey, iv: ByteArray,
                    algorithm: String = ALGORITHM, keyAlgorithm: String = KEY_ALOGRITHM): String {
            val cipher = Cipher.getInstance(algorithm)
            val keySpec = SecretKeySpec(key.encoded, keyAlgorithm)
            val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec)

            return cipher.doFinal(encrypted).decodeToString()
        }

        fun generateIV(): ByteArray {
            val iv = ByteArray(GCM_IV_LENGTH)
            val random = SecureRandom()
            random.nextBytes(iv)

            return iv
        }

        fun generateKey(keyAlgorithm: String = KEY_ALOGRITHM): SecretKey {
            val keyGenerator = KeyGenerator.getInstance(keyAlgorithm)
            keyGenerator.init(AES_KEY_SIZE)

            return keyGenerator.generateKey()
        }
    }
}
