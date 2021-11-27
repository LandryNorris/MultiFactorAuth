package com.landry.shared.http

import com.landry.shared.http.params.RefreshParams
import com.landry.shared.http.responses.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class Client {
    companion object {
        var bearerTokens: BearerTokens? = null
        val baseUrl = "localhost:8080"
        val client = HttpClient {
            installJson()
            install(Logging)

            install(UserAgent) {
                agent = "2Factor-mobile"
            }

            install(Auth) {
                bearer {
                    refreshTokens {
                        val tokens = bearerTokens
                        if(tokens == null) null
                        else {
                            val client = Client()
                            val refreshParams = RefreshParams(tokens.refreshToken, "")
                            val refreshResponse: LoginResponse = client.postJson("/refresh", refreshParams)
                            BearerTokens(refreshResponse.token, refreshResponse.refreshToken)
                        }
                    }
                }
            }
        }

        private fun HttpClientConfig<*>.installJson() {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    suspend inline fun <reified T> getJson(url: String): T {
        val response: HttpResponse = client.get(url)
        return response.receive()
    }

    suspend inline fun <reified T, reified S: Any> postJson(url: String, content: S): T {
        val response: HttpResponse = client.post(url) {
            header(HttpHeaders.Accept, ContentType.Application.Json)
            body = content
        }
        return response.receive()
    }
}