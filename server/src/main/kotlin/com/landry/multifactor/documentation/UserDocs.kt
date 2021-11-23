package com.landry.multifactor.documentation

import com.landry.multifactor.models.LoginResponse
import com.landry.multifactor.models.UserResponse
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RegistrationParams
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

val loginExample = LoginParams("email@email.com", "A password")
val registrationExample = RegistrationParams("email@email.com", "first", "last", "A password")

val loginDocs = MethodInfo.PostInfo<Unit, LoginParams, LoginResponse>(
    summary = "Log in to an account.",
    requestInfo = RequestInfo("Login Credentials", examples = mapOf("example" to loginExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Login Token")
)

val registrationDocs = MethodInfo.PostInfo<Unit, RegistrationParams, UserResponse>(
    summary = "Register a new user",
    requestInfo = RequestInfo("User info", examples = mapOf("example" to registrationExample))
)