package com.landry.multifactor.routes

import com.landry.multifactor.documentation.createDeviceDocs
import com.landry.multifactor.documentation.deactivateDeviceDocs
import com.landry.multifactor.documentation.getDeviceDocs
import com.landry.multifactor.documentation.queryDevicesDocs
import com.landry.multifactor.notarizedPostRoute
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.repos.DeviceRepository
import com.landry.multifactor.responses.toResponse
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

        baseDeviceRoutes(repo)

        route("{id}") {
            deviceByIdRoutes(repo)
        }
    }
}

private fun Route.deviceByIdRoutes(repo: DeviceRepository) {
    notarizedGet(getDeviceDocs) {
        val id = call.parameters["id"] ?: throw IllegalArgumentException("id must not be null")
        val device = repo.getDevice(id)!!.decrypt().toResponse()
        call.respond(HttpStatusCode.OK, device)
    }

    notarizedPostRoute("deactivate", deactivateDeviceDocs) {
        val id = call.parameters["id"] ?: throw IllegalArgumentException("id must not be null")
        repo.deactivateDevice(id)
        call.respond(HttpStatusCode.OK)
    }
}

private fun Route.baseDeviceRoutes(repo: DeviceRepository) {
    notarizedPost(createDeviceDocs) {
        val params = call.receive<DeviceParams>()
        val registeredDeviceResponse = repo.registerDevice(params)!!
        call.respond(HttpStatusCode.Created, registeredDeviceResponse)
    }

    notarizedGet(queryDevicesDocs) {
        val userId = call.parameters["userId"]
        val mac = call.parameters["mac"]
        val isActive = call.parameters["isActive"]?.lowercase()?.toBooleanStrictOrNull()
        val name = call.parameters["name"]
        val devices = repo.queryDevices(QueryDeviceParams(userId, mac, name, isActive)).map { it.decrypt().toResponse() }
        call.respond(devices)
    }
}