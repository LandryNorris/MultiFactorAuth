package com.landry.shared.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface OtpScreen {
    val state: Value<OtpComponent.OtpState>
}

class OtpComponent(context: ComponentContext): OtpScreen, ComponentContext by context {
    data class OtpState(val codes: List<String> = listOf())

    private val mutableState = MutableValue(OtpState())
    override val state = mutableState as Value<OtpState>
}