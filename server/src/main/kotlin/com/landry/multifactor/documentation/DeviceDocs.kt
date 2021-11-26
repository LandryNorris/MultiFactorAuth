package com.landry.multifactor.documentation

import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.responses.DeviceResponse
import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

class GetDevicesParams(@KompendiumParam(ParamType.QUERY) val userId: String)

val exampleDevicesList = listOf(DeviceResponse("id", "mac", "userId", "deviceName", true))
val exampleCreateDeviceParams = DeviceParams("mac", "userId", "deviceName")

val getDeviceDocs = MethodInfo.GetInfo(
    summary = "Get device with a given id",
    parameterExamples = mapOf("example" to GetDevicesParams("")),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "device with this id.", examples = mapOf("example" to exampleDevicesList)),
    tags = setOf("devices")
)

val createDeviceDocs = MethodInfo.PostInfo<Unit, DeviceParams, DeviceResponse>(
    summary = "Create a new device for a user",
    responseInfo = ResponseInfo(HttpStatusCode.OK, description = "Created device", examples = mapOf("example" to exampleDevicesList.first())),
    requestInfo = RequestInfo("Parameters to create the device", examples = mapOf("example" to exampleCreateDeviceParams)),
    tags = setOf("devices")
)