package com.landry.multifactor.plugins

import io.micrometer.prometheus.*
import io.ktor.metrics.micrometer.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureMonitoring() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        // ...
    }

    routing {
        get("/metrics-micrometer") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
