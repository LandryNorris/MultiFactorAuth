package com.landry.shared.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.landry.multifactor.Otp
import com.landry.shared.repositories.OtpRepository

interface OtpScreenComponent {
    val state: Value<OtpComponent.OtpState>
}

class OtpComponent(context: ComponentContext): OtpScreenComponent, ComponentContext by context {
    data class OtpState(val codes: List<Otp> = listOf())

    private val repo = OtpRepository()

    private val mutableState = repo.watchOtp()
    override val state = mutableState as Value<OtpState>
}