package com.landry.multifactor.unit

import com.landry.multifactor.defaultJwtConfig
import com.landry.multifactor.runKtorTest
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import org.junit.Test
import kotlin.test.assertEquals

class TestAuth {
    @Test
    fun testCallSecureRouteWithoutAuth() = runKtorTest(config = defaultJwtConfig) {
        val response = handleRequest(HttpMethod.Get, "/users").response
        assertEquals(HttpStatusCode.Unauthorized, response.status())
    }
}
