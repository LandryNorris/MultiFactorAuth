package com.landry.multifactor.documentation

import com.landry.multifactor.models.Device
import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

class GetDevicesParams(@KompendiumParam(ParamType.QUERY) val userId: String)

val exampleDevicesList = listOf(Device("id", "userId", "deviceName", "publicKey"))

val getDeviceDocs = MethodInfo.GetInfo(
    summary = "Get devices for a userId",
    parameterExamples = mapOf("example" to GetDevicesParams("")),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "list of devices associated with this user.", examples = mapOf("example" to exampleDevicesList))
)