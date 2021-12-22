package com.landry.multifactor.integration

import com.landry.multifactor.defaultConfig
import com.landry.multifactor.faker
import com.landry.multifactor.json
import com.landry.multifactor.jwt
import com.landry.multifactor.params.LoginParams
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.randomEmail
import com.landry.multifactor.responses.LoginResponse
import com.landry.multifactor.responses.RefreshResponse
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.runKtorTest
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MockUserRouteTest {
    private val jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZWF0IjoyNTE2MjM5MDIyLCJhdWQiOiJqd3QgYXVkaWVuY2" +
            "UiLCJpc3MiOiJodHRwczovL2p3dC1wcm92aWRlci1kb21haW4vIn0." +
            "5PrJN5hkkr-BBPc2v43XaNXbCR98mf9r2p-pIpohTj8"

    @Test
    fun testRegisterUser() {
        runKtorTest(config = defaultConfig) {
            val userResponse = registerUser()
            assertTrue(userResponse.id.isNotEmpty())
        }
    }

    @Test
    fun testRegisterAndGetUser() {
        runKtorTest(config = defaultConfig) {
            val registerResponse = registerUser()
            assertTrue(registerResponse.id.isNotEmpty())

            val userResponse = getUser(registerResponse.email)
            assertNotNull(userResponse)
        }
    }

    @Test
    fun testRegisterInvalidParams() {
        runKtorTest(config = defaultConfig) {
            val response = handleRequest(HttpMethod.Post, "/register") {
                jwt(jwt)
                setBody("""{"a value": 0, "invalid": "json"}""")
            }.response
            assertEquals(HttpStatusCode.UnsupportedMediaType, response.status())
        }
    }

    @Test
    fun testLogin() {
        runKtorTest(config = defaultConfig) {
            val params = randomRegistrationParams()
            registerUser(params)
            val loginResponse = login(params.email, params.password)
            assertNotNull(loginResponse)
        }
    }

    @Test
    fun testRefreshToken() {
        runKtorTest(config = defaultConfig) {
            val params = randomRegistrationParams()
            registerUser(params)
            val loginResponse = login(params.email, params.password)
            val refreshToken = refresh(loginResponse!!.refreshToken, params.email)
            assertNotNull(refreshToken)
        }
    }

    @Test
    fun testInvalidLogin() {
        runKtorTest(config = defaultConfig) {
            val loginResponse = login("email", "password", HttpStatusCode.Unauthorized)
            assertNull(loginResponse)
        }
    }

    @Test
    fun testGetUserByMissingEmail() {
        runKtorTest(config = defaultConfig) {
            val response = handleRequest(HttpMethod.Get, "/users") {
                jwt(jwt)
            }.response
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    private fun TestApplicationEngine.registerUser(params: RegistrationParams = randomRegistrationParams(),
                                                   expectedResult: HttpStatusCode = HttpStatusCode.OK): UserResponse {
        val response = handleRequest(HttpMethod.Post, "/register") {
            jwt(jwt)
            json(params, RegistrationParams.serializer())
        }.response
        assertEquals(expectedResult, response.status())

        return Json.decodeFromString(response.content ?: "null")
    }

    private fun TestApplicationEngine.getUser(email: String,
                                              expectedResult: HttpStatusCode = HttpStatusCode.OK): UserResponse? {
        val response = handleRequest(HttpMethod.Get, "/users?email=$email") {
            jwt(jwt)
        }.response
        assertEquals(expectedResult, response.status())

        return Json.decodeFromString(response.content ?: "null")
    }

    private fun TestApplicationEngine.login(email: String, password: String,
                                            expectedResult: HttpStatusCode = HttpStatusCode.OK): LoginResponse? {
        val response = handleRequest(HttpMethod.Post, "/login") {
            jwt(jwt)
            json(LoginParams(email, password), LoginParams.serializer())
        }.response
        assertEquals(expectedResult, response.status())
        return Json.decodeFromString(response.content ?: "null")
    }

    private fun TestApplicationEngine.refresh(token: String, email: String,
                                              expectedResult: HttpStatusCode = HttpStatusCode.OK): RefreshResponse? {
        val response = handleRequest(HttpMethod.Post, "/refresh") {
            jwt(jwt)
            json(RefreshParams(token, email), RefreshParams.serializer())
        }.response
        assertEquals(expectedResult, response.status())
        return Json.decodeFromString(response.content ?: "null")
    }

    private fun randomRegistrationParams(): RegistrationParams {
        return RegistrationParams(
            randomEmail(),
            faker.name.firstName(),
            faker.name.lastName(),
            faker.cultureSeries.cultureShips())
    }
}
