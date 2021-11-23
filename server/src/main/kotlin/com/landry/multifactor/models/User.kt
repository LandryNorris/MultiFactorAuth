package com.landry.multifactor.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class User(@Transient override var id: String = "", var email: String, var firstName: String, var lastName: String, val passwordHash: String): IdAble