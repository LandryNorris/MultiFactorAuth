package com.landry.shared

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val powersOfTen = arrayOf(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000)

fun Long.toBytes(): ByteArray {
    return byteArrayOf(
        this.and(0xFF.toLong().shl(56)).shr(56).toByte(),
        this.and(0xFF.toLong().shl(48)).shr(48).toByte(),
        this.and(0xFF.toLong().shl(40)).shr(40).toByte(),
        this.and(0xFF.toLong().shl(32)).shr(32).toByte(),
        this.and(0xFF.toLong().shl(24)).shr(24).toByte(),
        this.and(0xFF.toLong().shl(16)).shr(16).toByte(),
        this.and(0xFF.toLong().shl(8)).shr(8).toByte(),
        this.and(0xFF.toLong().shl(0)).shr(0).toByte(),
    )
}

suspend fun <T: Any> Flow<T>.collectAsValue(default: T, context: CoroutineContext = EmptyCoroutineContext): Value<T> {
    val result = MutableValue(default)
    if (context == EmptyCoroutineContext) {
        collect { result.value = it }
    } else withContext(context) {
        collect { result.value = it }
    }
    return result
}

fun <T : Any> Query<T>.asValue(): Value<Query<T>> {
    val result = MutableValue(this)

    val listener = object : Query.Listener {
        override fun queryResultsChanged() {
            result.value = this@asValue
        }
    }
    addListener(listener)
    return result
}