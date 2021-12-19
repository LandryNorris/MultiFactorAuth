package com.landry.multifactor

import com.landry.multifactor.models.IdAble
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Query
import io.bkbn.kompendium.Notarized.notarizedGet
import io.bkbn.kompendium.Notarized.notarizedPost
import io.bkbn.kompendium.models.meta.MethodInfo
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.DeserializationStrategy
import org.koin.core.context.GlobalContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.crypto.SecretKey

suspend fun <T> DocumentReference.await(strategy: DeserializationStrategy<T>): T? {
    val snapshot = get()
    return snapshot.data(strategy).also { if(it is IdAble) it.id = snapshot.id }
}

fun <T> DocumentSnapshot.await(strategy: DeserializationStrategy<T>): T? {
    return data(strategy).also { if(it is IdAble) it.id = id }
}

suspend fun <T> Query.await(strategy: DeserializationStrategy<T>): List<T> {
    return get().documents.mapNotNull { it.await(strategy) }
}

inline fun <reified T: Any, reified S: Any>
        Route.notarizedGetRoute(path: String, docs: MethodInfo.GetInfo<T, S>,
                                noinline body: PipelineInterceptor<Unit, ApplicationCall>) = route(path) {
    notarizedGet(docs, body)
}

inline fun <reified T: Any, reified S: Any, reified R: Any>
        Route.notarizedPostRoute(path: String, docs: MethodInfo.PostInfo<T, S, R>,
                                 noinline body: PipelineInterceptor<Unit, ApplicationCall>) = route(path) {
    notarizedPost(docs, body)
}

val koin by lazy { GlobalContext.get() }
inline fun <reified T : Any> inject() = koin.inject<T>()

fun LocalDateTime.toDate(): Date = Date.from(toInstant(ZoneOffset.UTC))

fun SecretKey.base64Encoded() = encoded.base64Encode()
fun ByteArray.base64Encode() = Base64.getEncoder().encode(this).decodeToString()
fun String.base64Decode(): ByteArray = Base64.getDecoder().decode(this)
