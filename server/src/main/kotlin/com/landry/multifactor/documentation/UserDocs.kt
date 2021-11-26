package com.landry.multifactor.documentation

import com.landry.multifactor.responses.LoginResponse
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.responses.UserResponse
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

val loginExample = LoginParams("email", "password")
val registrationExample = RegistrationParams("email", "first", "last", "password")
val exampleUserResponse = UserResponse("id", "first", "last", "email")

val loginDocs = MethodInfo.PostInfo<Unit, LoginParams, LoginResponse>(
    summary = "Log in to an account.",
    requestInfo = RequestInfo("Login Credentials", examples = mapOf("example" to loginExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Login Token"),
    tags = setOf("users")
)

val registrationDocs = MethodInfo.PostInfo<Unit, RegistrationParams, UserResponse>(
    summary = "Register a new user",
    requestInfo = RequestInfo("User info", examples = mapOf("example" to registrationExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Created user", examples = mapOf("example" to exampleUserResponse)),
    tags = setOf("users")
)