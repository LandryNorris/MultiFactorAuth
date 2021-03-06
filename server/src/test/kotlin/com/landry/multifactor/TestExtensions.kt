package com.landry.multifactor

import com.landry.multifactor.models.Device
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.plugins.*
import com.landry.multifactor.utils.EncryptionHelper
import io.github.serpro69.kfaker.faker
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.util.*

fun Application.setup() {
    configureKoinTest()
    configureSecurity()
    configureDocumentation()
    configureHTTP()
    configureSerialization()
    configureAdministration()
    configureStatusPages()
    configureRouting()
}

val defaultJwtConfig = mapOf(
    "jwt.secret" to "abcdefg",
    "jwt.audience" to "jwt audience",
    "jwt.realm" to "server",
    "jwt.domain" to "https://jwt-provider-domain/"
)

val defaultEncryptionConfig = mapOf(
    "encryption.strongKey" to EncryptionHelper().generateKey().base64Encoded()
)

val defaultConfig = buildMap {
    putAll(defaultJwtConfig)
    putAll(defaultEncryptionConfig)
}

fun <R> runKtorTest(config: Map<String, String> = mapOf(), test: suspend TestApplicationEngine.() -> R) {
    withTestApplication {
        (environment.config as MapApplicationConfig).apply {
            config.forEach { put(it.key, it.value) }
        }
        application.setup()
        runBlocking {
            test()
        }
    }
}

fun TestApplicationRequest.jwt(jwt: String) {
    addHeader(HttpHeaders.Authorization, "Bearer $jwt")
}

fun <T> TestApplicationRequest.json(value: T, strategy: SerializationStrategy<T>) {
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    setBody(Json.encodeToString(strategy, value))
}

val faker = faker {  }

fun randomDeviceParams() = DeviceParams(
    randomMacAddress(),
    randomId(),
    faker.device.modelName())

fun randomNewDevice() = Device(
    id = "",
    mac = randomMacAddress(),
    userId = randomId(),
    deviceName = faker.device.modelName(),
    secret = "",
    iv = "",
    isActive = false
)

fun randomEmail(): String {
    val first = faker.name.firstName()
    val last = faker.name.lastName()

    return if(faker.random.nextBoolean()) {
        "$first${faker.random.nextInt()}@${randomDomain()}"
    } else {
        "$first.$last@${randomDomain()}"
    }
}

fun randomDomain() = listOf(
    "gmail.com", "msn.com", "verizon.net", "live.com", "outlook.com", "yahoo.com"
).random()

fun randomId() = faker.random.randomString(length = 20, locale = Locale.US)

fun randomHexByteString() = faker.random.nextInt(256).toString(16)

fun randomMacAddress() = (0 until 6).joinToString(separator = ":") { randomHexByteString() }

fun startKoinForConfig(config: Map<String, String> = defaultJwtConfig) {
    val module = module {
        single<ApplicationConfig> {
            MapApplicationConfig().apply {
                config.forEach { put(it.key, it.value) }
            }
        }
    }
    org.koin.core.context.startKoin {
        modules(module)
    }
}
