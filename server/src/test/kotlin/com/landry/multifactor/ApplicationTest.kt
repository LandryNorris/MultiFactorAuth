package com.landry.multifactor

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testRoot() {
        runKtorTest(config = defaultJwtConfig) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("MultiFactorAPI", response.content)
            }
        }
    }
}
