package com.landry.multifactor.routes

import com.landry.multifactor.datasource.FirebaseDeviceDataSource
import com.landry.multifactor.documentation.getDeviceDocs
import io.bkbn.kompendium.Notarized.notarizedGet
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.deviceRoutes() {
    route("devices") {
        val repo by inject<FirebaseDeviceDataSource>()
        notarizedGet(getDeviceDocs) {
        }
    }
}