package com.landry.shared.otp

import com.landry.shared.toBytes

class Totp(secret: String, private val timeStep: Int = 30, codeLength: Int = 6): BaseOtp(secret, codeLength) {
    private var overwrittenTime: Long? = null

    override fun getValue(): ByteArray {
        return getT().toBytes()
    }

    fun setTime(newTimeSeconds: Long) {
        overwrittenTime = newTimeSeconds
    }

    fun getT(): Long {
        val time = getTime()
        return time/timeStep
    }

    /**
     * returns the time IN SECONDS to use in the calculation of the pin.
     */
    private fun getTime(): Long {
        return if(overwrittenTime != null) {
            overwrittenTime!!
        } else {
            0
        }
    }
}