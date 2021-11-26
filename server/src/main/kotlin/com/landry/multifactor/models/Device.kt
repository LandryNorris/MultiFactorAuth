package com.landry.multifactor.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Device(@Transient override var id: String = "", val mac: String, val userId: String, val deviceName: String, val secret: String, val iv: String, val isActive: Boolean): IdAble