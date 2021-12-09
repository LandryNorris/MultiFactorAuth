package com.landry.shared.routers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.landry.shared.components.LoginComponent
import com.landry.shared.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Root {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class Login(val component: LoginComponent): Child()
    }
}

class RootComponent(context: ComponentContext): Root, ComponentContext by context {
    private val router = router<Config, Root.Child>(
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private val currentUser = MutableStateFlow<User?>(null)

    override val routerState: Value<RouterState<*, Root.Child>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Root.Child =
        when (config) {
            is Config.Login -> Root.Child.Login(loginComponent(componentContext))
            is Config.Register -> TODO()
        }

    private fun loginComponent(componentContext: ComponentContext): LoginComponent =
        LoginComponent(componentContext, onUserReceived = { currentUser.value = it }, onRegisterPressed = { router.push(Config.Register) })

    private sealed class Config: Parcelable {
        @Parcelize
        object Login: Config()
        @Parcelize
        object Register: Config()
    }
}