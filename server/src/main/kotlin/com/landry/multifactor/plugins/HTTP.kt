package com.landry.multifactor.plugins

import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.application.*

fun Application.configureHTTP() {
    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(
                    CacheControl.MaxAge(maxAgeSeconds = HTTPConfig.Caching.maxAgeSeconds)
                )
                else -> null
            }
        }
    }
    install(Compression) {
        gzip {
            priority = HTTPConfig.Compression.Gzip.priority
        }
        deflate {
            priority = HTTPConfig.Compression.Deflate.priority
            minimumSize(HTTPConfig.Compression.Deflate.minSize)
        }
    }

    install(ConditionalHeaders)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        host(HTTPConfig.Cors.host)
    }
    install(DefaultHeaders) {
        header("X-Engine", HTTPConfig.DefaultHeaders.engine)
    }
    install(PartialContent) {
        // Maximum number of ranges that will be accepted from an HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = HTTPConfig.PartialContent.maxRangeCount
    }
}

object HTTPConfig {
    object Caching {
        const val maxAgeSeconds = 24 * 60 * 60
    }

    object Compression {
        object Gzip {
            const val priority = 1.0
        }
        object Deflate {
            const val priority = 10.0
            const val minSize = 1024L
        }
    }

    object Cors {
        const val host = "localhost:8080"
    }

    object PartialContent {
        const val maxRangeCount = 10
    }

    object DefaultHeaders {
        const val engine = "Ktor"
    }
}
