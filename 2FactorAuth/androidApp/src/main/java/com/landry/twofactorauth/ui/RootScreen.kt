package com.landry.twofactorauth.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.landry.shared.routers.Root

@Composable
fun RootScreen(rootComponent: Root) {
    Children(
        routerState = rootComponent.routerState,
        animation = crossfade()
    ) {
        when(val child = it.instance) {
            is Root.Child.Login -> LoginScreen(child.component)
        }
    }
}