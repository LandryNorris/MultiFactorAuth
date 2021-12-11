package com.landry.shared.otp

import com.landry.shared.toBytes

class Hotp(secret: String, private var counter: Long, codeLength: Int = 6): BaseOtp(secret, codeLength) {

    override fun getValue(): ByteArray {
        return counter.toBytes()
    }

    fun incrementCounter() {
        counter++
    }
}