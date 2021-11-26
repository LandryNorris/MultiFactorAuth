package com.landry.multifactor.routes

import com.landry.multifactor.documentation.createDeviceDocs
import com.landry.multifactor.documentation.getDeviceDocs
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.repos.DeviceRepository
import io.bkbn.kompendium.Notarized.notarizedGet
import io.bkbn.kompendium.Notarized.notarizedPost
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.deviceRoutes() {
    route("devices") {
        val repo by inject<DeviceRepository>()
        notarizedGet(getDeviceDocs) {
            val id = call.parameters["id"]

            if(id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@notarizedGet
            }

            val device = repo.getDevice(id)

            if(device == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, device)
            }
        }

        notarizedPost(createDeviceDocs) {
            val params = call.receive<DeviceParams>()
            val registeredDeviceResponse = repo.registerDevice(params)!!

            call.respond(HttpStatusCode.Created, registeredDeviceResponse)
        }
    }
}