package com.landry.multifactor.documentation

import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.responses.DeviceResponse
import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

class GetDevicesParams(@KompendiumParam(ParamType.PATH) val id: String)

val exampleDevicesList = listOf(DeviceResponse("id", "mac", "userId", "deviceName", true))
val exampleCreateDeviceParams = DeviceParams("mac", "userId", "deviceName")

val getDeviceDocs = MethodInfo.GetInfo(
    summary = "Get device with a given id",
    parameterExamples = mapOf("example" to GetDevicesParams("id")),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "device with this id.", examples = mapOf("example" to exampleDevicesList.first())),
    canThrow = setOf(IllegalArgumentException::class, NullPointerException::class),
    securitySchemes = setOf("jwt"),
    tags = setOf("devices")
)

val createDeviceDocs = MethodInfo.PostInfo<Unit, DeviceParams, DeviceResponse>(
    summary = "Create a new device for a user",
    responseInfo = ResponseInfo(HttpStatusCode.OK, description = "Created device", examples = mapOf("example" to exampleDevicesList.first())),
    requestInfo = RequestInfo("Parameters to create the device", examples = mapOf("example" to exampleCreateDeviceParams)),
    securitySchemes = setOf("jwt"),
    tags = setOf("devices")
)

val queryDevicesDocs = MethodInfo.GetInfo(
    summary = "Get a list of devices matching the given queries",
    parameterExamples = mapOf("example" to QueryDeviceParams(null, null, null, null)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Devices matching queries", examples = mapOf("example" to exampleDevicesList)),
    canThrow = setOf(IllegalArgumentException::class, NullPointerException::class),
    securitySchemes = setOf("jwt"),
    tags = setOf("devices")
)

val deactivateDeviceDocs = MethodInfo.PostInfo<GetDevicesParams, Unit, Unit>(
    summary = "Deactivate the device with the given id",
    parameterExamples = mapOf("example" to GetDevicesParams("id")),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "successful operation", examples = mapOf()),
    canThrow = setOf(IllegalArgumentException::class, NullPointerException::class),
    securitySchemes = setOf("jwt"),
    tags = setOf("devices")
)