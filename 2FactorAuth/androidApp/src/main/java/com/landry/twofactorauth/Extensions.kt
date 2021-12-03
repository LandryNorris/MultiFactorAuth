package com.landry.twofactorauth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backpressed.BackPressedDispatcher
import com.arkivanov.essenty.backpressed.BackPressedHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

fun launch(scope: CoroutineScope, block: KSuspendFunction0<Unit>): () -> Unit {
    return { scope.launch { block() } }
}
