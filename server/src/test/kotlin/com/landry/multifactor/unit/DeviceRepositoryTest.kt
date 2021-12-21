package com.landry.multifactor.unit

import com.landry.multifactor.base64Encoded
import com.landry.multifactor.datasource.MockDeviceDataSource
import com.landry.multifactor.faker
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.randomDeviceParams
import com.landry.multifactor.repos.DeviceRepository
import com.landry.multifactor.startKoinForConfig
import com.landry.multifactor.utils.EncryptionHelper
import io.ktor.config.ApplicationConfig
import io.ktor.config.MapApplicationConfig
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DeviceRepositoryTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startKoinForConfig(config = mapOf("encryption.strongKey" to EncryptionHelper.generateKey().base64Encoded()))
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            stopKoin()
        }
    }


    @Test
    fun testRegisteringDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource())
        val deviceParams = randomDeviceParams()
        val deviceResponse = repo.registerDevice(deviceParams)

        assertNotNull(deviceResponse)
        assertTrue(deviceResponse.device.deviceId.isNotEmpty())
    }

    @Test
    fun testRegisterAndGetDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource())
        val deviceParams = randomDeviceParams()
        val deviceResponse = repo.registerDevice(deviceParams)
        assertNotNull(deviceResponse)
        assertTrue(deviceResponse.device.deviceId.isNotEmpty())

        val foundDevice = repo.getDevice(deviceResponse.device.deviceId)
        assertNotNull(foundDevice)
        assertEquals(deviceResponse.device.deviceId, foundDevice.id)
    }

    @Test
    fun testQueryDevices() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource())
        val devices = listOf(
            DeviceParams("EB:7E:65:5C:49:00", "abcd", "Google Pixel 3"),
            DeviceParams("EB:7E:65:5C:49:01", "abcd", "Google Pixel 3"),
            DeviceParams("AB:CD:EF:12:34:56", "abcd", "Google Pixel 5"),
            DeviceParams("AB:CD:EF:12:34:12", "efgh", "MacBook Air"),
            DeviceParams("59:6B:0A:AC:DD:92", "efgh", "Google Pixel 3"),
            DeviceParams("60:6B:0A:AC:DD:92", "1234", "Google Pixel 3"))

        devices.forEach { repo.registerDevice(it) }

        //Test Null Query
        val exception = try {
            repo.queryDevices(QueryDeviceParams())
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        assertNotNull(exception)
        assertTrue(exception.message?.isNotEmpty() == true)

        val devicesForUserAbcd = repo.queryDevices(QueryDeviceParams(userId = "abcd"))
        assertEquals(3, devicesForUserAbcd.size)

        val allActiveDevices = repo.queryDevices(QueryDeviceParams(active = true))
        assertEquals(6, allActiveDevices.size)

        val devicesByMac = repo.queryDevices(QueryDeviceParams(mac = "EB:7E:65:5C:49:00"))
        assertEquals(1, devicesByMac.size)

        val allParamsResult = repo.queryDevices(QueryDeviceParams(mac = "60:6B:0A:AC:DD:92", userId = "1234", active = true))
        assertEquals(1, allParamsResult.size)

        val noMatchingDeviceResult = repo.queryDevices(QueryDeviceParams(mac = "12:34:56:78:ab:cd"))
        assertEquals(0, noMatchingDeviceResult.size)
    }

    @Test
    fun testCreateAndDeactivateDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource())
        val deviceParams = randomDeviceParams()
        val deviceResponse = repo.registerDevice(deviceParams)
        assertNotNull(deviceResponse)

        repo.deactivateDevice(deviceResponse.device.deviceId)

        val newDeviceResponse = repo.getDevice(deviceResponse.device.deviceId)
        assertNotNull(newDeviceResponse)
        assertFalse(newDeviceResponse.isActive)
    }
}
