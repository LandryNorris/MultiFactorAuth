package com.landry.multifactor.integration

import com.landry.multifactor.defaultConfig
import com.landry.multifactor.json
import com.landry.multifactor.jwt
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.randomDeviceParams
import com.landry.multifactor.responses.CreateDeviceResponse
import com.landry.multifactor.responses.DeviceResponse
import com.landry.multifactor.runKtorTest
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MockDeviceRouteTest {
    private val jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZWF0IjoyNTE2MjM5MDIyLCJhdWQiOiJqd3QgYXVkaWVuY2" +
            "UiLCJpc3MiOiJodHRwczovL2p3dC1wcm92aWRlci1kb21haW4vIn0." +
            "5PrJN5hkkr-BBPc2v43XaNXbCR98mf9r2p-pIpohTj8"

    @Test
    fun testRegisterDevice() {
        runKtorTest(config = defaultConfig) {
            val deviceParams = randomDeviceParams()
            val deviceResponse = register(deviceParams)
            assertNotNull(deviceResponse)
        }
    }

    @Test
    fun testRegisterAndGetDevice() {
        runKtorTest(config = defaultConfig) {
            val deviceParams = randomDeviceParams()
            val registrationResponse = register(deviceParams)

            val deviceResponse = getDeviceById(registrationResponse.device.deviceId)
            assertNotNull(deviceResponse)
        }
    }

    @Test
    fun testRegisterAndDeactivateDevice() {
        runKtorTest(config = defaultConfig) {
            val deviceParams = randomDeviceParams()
            val registrationResponse = register(deviceParams)

            val deviceResponse = deactivate(registrationResponse.device.deviceId)
            assertNotNull(deviceResponse)
            assertFalse(deviceResponse.isActive)
        }
    }

    @Test
    fun testQueryDevices() {
        val params = listOf(
            DeviceParams("EB:7E:65:5C:49:00", "abcd", "Google Pixel 3"),
            DeviceParams("EB:7E:65:5C:49:01", "abcd", "Google Pixel 3"),
            DeviceParams("AB:CD:EF:12:34:56", "abcd", "Google Pixel 5"),
            DeviceParams("AB:CD:EF:12:34:12", "efgh", "MacBook Air"),
            DeviceParams("59:6B:0A:AC:DD:92", "efgh", "Google Pixel 3"),
            DeviceParams("60:6B:0A:AC:DD:92", "1234", "Google Pixel 3")
        )
        runKtorTest(config = defaultConfig) {
            params.forEach {
                register(it)
            }
            val queryMac = query(QueryDeviceParams(mac = "AB:CD:EF:12:34:56"))
            assertEquals(1, queryMac.size)

            val queryUser = query(QueryDeviceParams(userId = "abcd"))
            assertEquals(3, queryUser.size)

            val queryActive = query(QueryDeviceParams(active = true))
            assertEquals(6, queryActive.size)

            //Test again after deactivating a random device.
            deactivate(queryActive.random().deviceId)
            val newQueryActive = query(QueryDeviceParams(active = true))
            assertEquals(5, newQueryActive.size)

            val emptyQueryResult = handleRequest(HttpMethod.Get, "/devices") {
                jwt(jwt)
            }.response
            assertEquals(HttpStatusCode.BadRequest, emptyQueryResult.status())

            val queryWithNoResults = query(QueryDeviceParams(userId = "non-existent user"))
            assertEquals(0, queryWithNoResults.size)
        }
    }

    @Test
    fun testGetNonExistentDevice() {
        runKtorTest(config = defaultConfig) {
            //test non-existent id
            var response = handleRequest(HttpMethod.Get, "/devices/invalidId") {
                jwt(jwt)
            }.response
            assertEquals(HttpStatusCode.NotFound, response.status())

            //test null id
            response = handleRequest(HttpMethod.Get, "/devices/null") {
                jwt(jwt)
            }.response
            assertEquals(HttpStatusCode.NotFound, response.status())

            //test missing id
            response = handleRequest(HttpMethod.Get, "/devices/") {
                jwt(jwt)
            }.response
            assertEquals(HttpStatusCode.NotFound, response.status())
        }
    }

    private fun TestApplicationEngine.register(
        params: DeviceParams, expected: HttpStatusCode = HttpStatusCode.Created): CreateDeviceResponse {
        val response = handleRequest(HttpMethod.Post, "/devices") {
            jwt(jwt)
            json(params, DeviceParams.serializer())
        }.response
        assertEquals(expected, response.status())
        return Json.decodeFromString(response.content ?: "null")
    }

    private fun TestApplicationEngine.getDeviceById(
        id: String, expected: HttpStatusCode = HttpStatusCode.OK): DeviceResponse? {
        val response = handleRequest(HttpMethod.Get, "/devices/$id") {
            jwt(jwt)
        }.response
        assertEquals(expected, response.status())
        return Json.decodeFromString(response.content ?: "null")
    }

    private fun TestApplicationEngine.deactivate(
        id: String, expected: HttpStatusCode = HttpStatusCode.OK): DeviceResponse? {
        val response = handleRequest(HttpMethod.Post, "/devices/$id/deactivate") {
            jwt(jwt)
        }.response
        assertEquals(expected, response.status())

        return getDeviceById(id)
    }

    private fun TestApplicationEngine.query(params: QueryDeviceParams): List<DeviceResponse> {
        val urlString = buildString {
            append("/devices?")
            params.mac?.run { append("mac=$this") }
            params.userId?.run { append("userId=$this") }
            params.active?.run { append("active=$this") }
        }.trimEnd('?')
        val response = handleRequest(HttpMethod.Get, urlString) {
            jwt(jwt)
        }.response
        assertEquals(HttpStatusCode.OK, response.status())

        return Json.decodeFromString(response.content ?: "null")
    }
}
