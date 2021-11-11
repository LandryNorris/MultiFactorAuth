package com.landry.multifactor.models

import kotlinx.serialization.Serializable

@Serializable
class User(@Transient override var id: String, var firstName: String, var lastName: String): IdAble