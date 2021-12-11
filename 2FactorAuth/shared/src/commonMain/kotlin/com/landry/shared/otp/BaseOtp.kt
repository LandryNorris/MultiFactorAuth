package com.landry.shared.otp

import com.landry.shared.powersOfTen
import com.soywiz.krypto.HMAC
import io.ktor.utils.io.core.*
import kotlin.experimental.and

abstract class BaseOtp(private val secret: String, private val codeLength: Int = 6) {

    private fun hash(value: ByteArray): ByteArray {
        val hash = HMAC.hmacSHA1(secret.toByteArray(), value)
        return hash.bytes
    }

    fun generatePin(): String {
        val hash = hash(getValue())

        val offset = hash.last().and(0x0F).toInt()

        val truncatedHash = hashToInt(hash, offset).and(0x7FFFFFFF)
        val pin = truncatedHash.mod(powersOfTen[codeLength])

        return pad(pin, codeLength)
    }

    private fun pad(pin: Int, length: Int): String {
        return buildString {
            val result = pin.toString()
            for(i in 0 until length-result.length) {
                append('0')
            }
            append(result)
        }
    }

    private fun hashToInt(bytes: ByteArray, start: Int): Int {
        val data = byteArrayOf(bytes[start], bytes[start+1], bytes[start+2], bytes[start+3]).map { it.toUByte().toInt() }
        return data[0].shl(24)
            .or(data[1].shl(16))
            .or(data[2].shl(8))
            .or(data[3])
    }

    abstract fun getValue(): ByteArray
}

sealed class OtpMethod {
    object HOTP: OtpMethod()
    object TOTP: OtpMethod()
}