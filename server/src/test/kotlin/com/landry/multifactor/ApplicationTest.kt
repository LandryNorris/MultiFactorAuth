package com.landry.multifactor

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.landry.multifactor.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        setupKtorTest {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("MultiFactorAPI", response.content)
            }
        }
    }
}
