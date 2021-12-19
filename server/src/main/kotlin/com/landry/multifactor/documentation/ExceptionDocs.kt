package com.landry.multifactor.documentation

import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

val authenticationExceptionDocs = ResponseInfo<Unit>(HttpStatusCode.Unauthorized, "User in not authenticated")

val authorizationExceptionDocs = ResponseInfo<Unit>(HttpStatusCode.Forbidden, "User in not authorized")

val emailExistsExceptionDocs = ResponseInfo<Unit>(HttpStatusCode.Conflict, "User in not authorized")

val notFoundExceptionDocs = ResponseInfo<Unit>(HttpStatusCode.NotFound, "Not Found")

val illegalArgumentDocs = ResponseInfo<Unit>(HttpStatusCode.BadRequest, "Missing or invalid arguments")
