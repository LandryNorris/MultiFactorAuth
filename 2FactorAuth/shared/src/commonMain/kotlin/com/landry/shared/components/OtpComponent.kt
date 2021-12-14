package com.landry.shared.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.landry.multifactor.Otp
import com.landry.shared.repositories.OtpRepository
import com.landry.shared.collectAsValue

interface OtpScreen {
    val state: Value<OtpComponent.OtpState>
}

class OtpComponent(context: ComponentContext): OtpScreen, ComponentContext by context {
    data class OtpState(val codes: List<Otp> = listOf())

    private val repo = OtpRepository()

    private val mutableState = repo.watchOtp()
    override val state = mutableState as Value<OtpState>
}