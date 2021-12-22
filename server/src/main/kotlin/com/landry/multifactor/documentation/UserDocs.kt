package com.landry.multifactor.documentation

import com.landry.multifactor.exceptions.AuthenticationException
import com.landry.multifactor.responses.LoginResponse
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.responses.RefreshResponse
import com.landry.multifactor.responses.UserResponse
import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

val loginExample = LoginParams("email", "password")
val registrationExample = RegistrationParams("email", "first", "last", "password")
val exampleUserResponse = UserResponse("id", "first", "last", "email")
val refreshExample = RefreshParams("refreshToken", "email")
val exampleRefreshResponse = RefreshResponse("token", "refreshToken")

val loginDocs = MethodInfo.PostInfo<Unit, LoginParams, LoginResponse>(
    summary = "Log in to an account.",
    requestInfo = RequestInfo("Login Credentials", examples = mapOf("example" to loginExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Login Token"),
    canThrow = setOf(AuthenticationException::class),
    tags = setOf("users")
)

val registrationDocs = MethodInfo.PostInfo<Unit, RegistrationParams, UserResponse>(
    summary = "Register a new user",
    requestInfo = RequestInfo("User info", examples = mapOf("example" to registrationExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Created user", examples = mapOf("example" to exampleUserResponse)),
    tags = setOf("users")
)

val refreshDocs = MethodInfo.PostInfo<Unit, RefreshParams, RefreshResponse>(
    summary = "Request a refresh token",
    requestInfo = RequestInfo("Refresh Token", examples = mapOf("example" to refreshExample)),
    responseInfo = ResponseInfo(HttpStatusCode.OK, "Refresh Response", examples = mapOf("example" to exampleRefreshResponse))
)

val getUserByEmailDocs = MethodInfo.GetInfo(
    summary = "Get a user by email",
    parameterExamples = mapOf("example" to GetUserByEmail("email")),
    responseInfo = ResponseInfo(HttpStatusCode.OK,
        description = "User with given email",
        examples = mapOf("example" to exampleUserResponse)),
    canThrow = setOf(IllegalArgumentException::class, NullPointerException::class),
    securitySchemes = setOf("jwt"),
    tags = setOf("users")
)

val rootDocs = MethodInfo.GetInfo<Unit, Unit>(
    summary = "Get the name of the API"
)

data class GetUserByEmail(@KompendiumParam(ParamType.QUERY) val email: String)
