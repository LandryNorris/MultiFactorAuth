package com.landry.twofactorauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.landry.shared.components.Login
import com.landry.shared.components.LoginComponent
import com.landry.twofactorauth.launch

@Composable
fun LoginScreen(component: Login) {
    val state by component.state.subscribeAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(state.email, onValueChange = component::setEmail, label = { Text("email") })
        TextField(state.password, onValueChange = component::setPassword, label = { Text("password") })
        Text(text = state.errorMessage, modifier = Modifier.fillMaxWidth(), color = Color.Red, textAlign = TextAlign.Center)
        Button(onClick = launch(coroutineScope, component::submit), content = { Text("Submit") })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = component::register, content = { Text("Register") })
        }
    }
}

@Preview
@Composable
fun LoginUiPreview() {
    LoginScreen(LoginPreview())
}

class LoginPreview: Login {
    override val state = MutableValue(LoginComponent.LoginState())
    override fun setEmail(email: String) {}
    override fun setPassword(password: String) {}
    override suspend fun submit() {}
    override fun register() {}
}