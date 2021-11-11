package com.landry.multifactor

import com.landry.multifactor.models.IdAble
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Query
import io.bkbn.kompendium.Notarized.notarizedGet
import io.bkbn.kompendium.models.meta.MethodInfo
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.DeserializationStrategy

suspend fun <T> DocumentReference.await(strategy: DeserializationStrategy<T>): T {
    val snapshot = get()
    return snapshot.data(strategy).also { if(it is IdAble) it.id = snapshot.id }
}

fun <T> DocumentSnapshot.await(strategy: DeserializationStrategy<T>): T {
    return data(strategy).also { if(it is IdAble) it.id = id }
}

suspend fun <T> Query.await(strategy: DeserializationStrategy<T>): List<T> {
    return get().documents.map { it.await(strategy) }
}

inline fun <reified T: Any, reified S: Any> Route.notarizedGetRoute(path: String, docs: MethodInfo.GetInfo<T, S>,
                                                                    noinline body: PipelineInterceptor<Unit, ApplicationCall>) = route(path) {
    notarizedGet(docs, body)
}