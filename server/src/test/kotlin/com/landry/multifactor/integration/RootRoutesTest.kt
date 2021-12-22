package com.landry.multifactor.integration

import com.landry.multifactor.defaultConfig
import com.landry.multifactor.runKtorTest
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import org.junit.Test
import kotlin.test.assertEquals

class RootRoutesTest {
    @Test
    fun testRootEndpoint() {
        runKtorTest(config = defaultConfig) {
            val result = handleRequest(HttpMethod.Get, "/").response
            assertEquals(HttpStatusCode.OK, result.status())
            assertEquals("MultiFactorAPI", result.content)
        }
    }
}
