package com.landry.multifactor.unit

import com.landry.multifactor.datasource.MockDeviceDataSource
import com.landry.multifactor.defaultConfig
import com.landry.multifactor.params.DeviceParams
import com.landry.multifactor.params.QueryDeviceParams
import com.landry.multifactor.putAll
import com.landry.multifactor.randomDeviceParams
import com.landry.multifactor.repos.DeviceRepository
import io.ktor.config.MapApplicationConfig
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeviceRepositoryTest {
    private val config = MapApplicationConfig().apply { putAll(defaultConfig) }

    @Test
    fun testRegisteringDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource(), config)
        val deviceParams = randomDeviceParams()
        val deviceResponse = repo.registerDevice(deviceParams)

        assertNotNull(deviceResponse)
        assertTrue(deviceResponse.device.deviceId.isNotEmpty())
    }

    @Test
    fun testRegisterAndGetDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource(), config)
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
        val repo = DeviceRepository(MockDeviceDataSource(), config)
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

        val allParamsResult = repo.queryDevices(
            QueryDeviceParams(mac = "60:6B:0A:AC:DD:92", userId = "1234", active = true))
        assertEquals(1, allParamsResult.size)

        val noMatchingDeviceResult = repo.queryDevices(QueryDeviceParams(mac = "12:34:56:78:ab:cd"))
        assertEquals(0, noMatchingDeviceResult.size)
    }

    @Test
    fun testCreateAndDeactivateDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource(), config)
        val deviceParams = randomDeviceParams()
        val deviceResponse = repo.registerDevice(deviceParams)
        assertNotNull(deviceResponse)

        repo.deactivateDevice(deviceResponse.device.deviceId)

        val newDeviceResponse = repo.getDevice(deviceResponse.device.deviceId)
        assertNotNull(newDeviceResponse)
        assertFalse(newDeviceResponse.isActive)
    }

    @Test
    fun testGetNonexistentDevice() = runBlocking {
        val repo = DeviceRepository(MockDeviceDataSource(), config)
        val device = repo.getDevice("invalid id")
        assertNull(device)
    }
}
