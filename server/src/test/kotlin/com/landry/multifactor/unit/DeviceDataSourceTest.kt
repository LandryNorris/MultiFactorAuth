package com.landry.multifactor.unit

import com.landry.multifactor.datasource.MockDeviceDataSource
import com.landry.multifactor.randomNewDevice
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeviceDataSourceTest {
    @Test
    fun testQueryNonExistentDevice() = runBlocking {
        val dataSource = MockDeviceDataSource()
        val device = dataSource.getDeviceById("An Invalid ID")
        assertNull(device)
    }

    @Test
    fun testRegisterDevice() = runBlocking {
        val dataSource = MockDeviceDataSource()
        val device = randomNewDevice()

        val deviceResponse = dataSource.registerDevice(device)

        assertNotNull(deviceResponse)
        assertTrue(deviceResponse.deviceId.isNotEmpty())
    }

    @Test
    fun testRegisterAndGetDevice() = runBlocking {
        val dataSource = MockDeviceDataSource()
        val device = randomNewDevice()
        val deviceResponse = dataSource.registerDevice(device)
        assertNotNull(deviceResponse)

        val foundDevice = dataSource.getDeviceById(deviceResponse.deviceId)
        assertNotNull(foundDevice)
        assertEquals(deviceResponse.deviceId, foundDevice.id)
    }

    @Test
    fun testGettingDeviceWhenMultipleInDataSource() = runBlocking {
        val dataSource = MockDeviceDataSource()
        val firstDevice = randomNewDevice()
        val firstDeviceResponse = dataSource.registerDevice(firstDevice)
        assertNotNull(firstDeviceResponse)

        val secondDevice = randomNewDevice()
        val secondDeviceResponse = dataSource.registerDevice(secondDevice)
        assertNotNull(secondDeviceResponse)

        val foundDevice = dataSource.getDeviceById(firstDeviceResponse.deviceId)
        assertNotNull(foundDevice)
        assertEquals(firstDeviceResponse.deviceId, foundDevice.id)

        val foundSecondDevice = dataSource.getDeviceById(secondDeviceResponse.deviceId)
        assertNotNull(foundSecondDevice)
        assertEquals(secondDeviceResponse.deviceId, foundSecondDevice.id)
    }
}
