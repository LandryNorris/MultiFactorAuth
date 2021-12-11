package com.landry.shared.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.landry.shared.http.Client
import com.landry.shared.http.exceptions.AuthenticationException
import com.landry.shared.models.User
import com.landry.shared.services.LoginService
import io.ktor.client.features.auth.providers.*

interface Login {
    val state: Value<LoginComponent.LoginState>
    fun setEmail(email: String)
    fun setPassword(password: String)
    suspend fun submit()
    fun register()
}

class LoginComponent(context: ComponentContext, private val onUserReceived: (User) -> Unit, private val onRegisterPressed: () -> Unit): Login, ComponentContext by context {
    private val mutableState = MutableValue(LoginState())
    override val state: Value<LoginState> = mutableState

    override fun setEmail(email: String) {
        mutableState.reduce { it.copy(email = email) }
    }

    override fun setPassword(password: String) {
        mutableState.reduce { it.copy(password = password) }
    }

    override suspend fun submit() {
        try {
            performLogin()
        } catch (e: AuthenticationException) {
            showIncorrectLogin()
        }
    }

    private fun showIncorrectLogin() {
        mutableState.reduce { it.copy(errorMessage = "That username or password is incorrect.") }
    }

    private suspend fun performLogin() {
        val loginResponse = LoginService().performLogin(state.value.email, state.value.password)
        onUserReceived(loginResponse.user.toUser())
        Client.bearerTokens = BearerTokens(loginResponse.token, loginResponse.refreshToken)
    }

    override fun register() = onRegisterPressed.invoke()

    data class LoginState(val email: String = "", val password: String = "", val errorMessage: String = "")
}