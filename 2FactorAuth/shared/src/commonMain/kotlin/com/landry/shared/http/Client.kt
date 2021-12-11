package com.landry.shared.http

import com.landry.shared.http.exceptions.AuthenticationException
import com.landry.shared.http.exceptions.AuthorizationException
import com.landry.shared.http.exceptions.NotFoundException
import com.landry.shared.http.params.RefreshParams
import com.landry.shared.http.responses.LoginResponse
import com.landry.shared.http.responses.RefreshResponse
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
        val baseUrl = "https://skilled-curve-329023.uc.r.appspot.com/"
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
                            val refreshResponse: RefreshResponse = client.postJson("/refresh", refreshParams)
                            BearerTokens(refreshResponse.token, refreshResponse.refreshToken)
                        }
                    }
                }
            }

            HttpResponseValidator {
                handleResponseException { exception ->
                    val clientException = exception as? ClientRequestException ?: return@handleResponseException
                    val response = clientException.response
                    if(response.status == HttpStatusCode.NotFound) throw NotFoundException()
                    //The standard is kind of weird in that the Unauthorized code actually means unauthenticated.
                    if(response.status == HttpStatusCode.Unauthorized) throw AuthenticationException()
                    //Forbidden is a better code for authorization
                    if(response.status == HttpStatusCode.Forbidden) throw AuthorizationException()

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
        val response: HttpResponse = client.get(getFullUrl(url))
        return response.receive()
    }

    suspend inline fun <reified T, reified S: Any> postJson(url: String, content: S): T {
        val response: HttpResponse = client.post(getFullUrl(url)) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = content
        }
        return response.receive()
    }

    fun getFullUrl(url: String): String {
        return baseUrl + url
    }
}