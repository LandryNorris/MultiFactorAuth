package com.landry.multifactor.params

import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import kotlinx.serialization.Serializable

@Serializable
data class QueryDeviceParams(
    @KompendiumParam(ParamType.QUERY) val userId: String?,
    @KompendiumParam(ParamType.QUERY) val mac: String?,
    @KompendiumParam(ParamType.QUERY) val name: String?,
    @KompendiumParam(ParamType.QUERY) val isActive: Boolean?)