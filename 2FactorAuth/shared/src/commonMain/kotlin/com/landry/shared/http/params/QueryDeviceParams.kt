package com.landry.shared.http.params

@Serializable
data class QueryDeviceParams(
    @KompendiumParam(ParamType.QUERY) val userId: String?,
    @KompendiumParam(ParamType.QUERY) val mac: String?,
    @KompendiumParam(ParamType.QUERY) val name: String?,
    @KompendiumParam(ParamType.QUERY) val isActive: Boolean?)