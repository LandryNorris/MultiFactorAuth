package com.landry.multifactor.utils

import java.util.*
import javax.crypto.KeyGenerator

class SecretGenerator {
    companion object {
        const val ALGORITHM = "HmacSHA512"

        fun generate(): String {
            val keyFactory = KeyGenerator.getInstance(ALGORITHM)
            val key = keyFactory.generateKey()
            return Base64.getEncoder().encodeToString(key.encoded)
        }
    }
}