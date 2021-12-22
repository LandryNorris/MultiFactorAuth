package com.landry.multifactor.unit

import com.landry.multifactor.datasource.MockUserDataSource
import com.landry.multifactor.defaultConfig
import com.landry.multifactor.encryptionKeyStrong
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import com.landry.multifactor.faker
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.putAll
import com.landry.multifactor.randomEmail
import com.landry.multifactor.repos.UserRepository
import io.ktor.config.MapApplicationConfig
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryTest {

    private val config = MapApplicationConfig().apply {
        putAll(defaultConfig)
    }

    @Test
    fun testRegisterUser() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)

        assertNotNull(userResponse)
        assertTrue(userResponse.id.isNotEmpty())
        assertEquals(userResponse.firstName, params.firstName)
        assertEquals(userResponse.lastName, params.lastName)
        assertEquals(userResponse.email, params.email)
    }

    @Test
    fun testUserDataIsEncrypted() = runBlocking {
        val userDataSource = MockUserDataSource()
        val userRepository = UserRepository(userDataSource, config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)

        assertNotNull(userResponse)
        val storedUser = userDataSource.getRawUsersList().first()
        assertTrue(storedUser.iv.isNotEmpty())
        assertEquals(params.email, storedUser.email) //email is stored in plain text for searching.
        assertNotEquals(params.firstName, storedUser.firstName)
        assertNotEquals(params.lastName, storedUser.lastName)

        val decryptedUser = storedUser.decrypt(config.encryptionKeyStrong())

        assertEquals(params.email, decryptedUser.email)
        assertEquals(params.firstName, decryptedUser.firstName)
        assertEquals(params.lastName, decryptedUser.lastName)
    }

    @Test
    fun testRegisterAndLogin() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)
        assertNotNull(userResponse)

        val loginResponse = userRepository.login(params.email, params.password)
        assertNotNull(loginResponse)

        assertTrue(loginResponse.token.isNotEmpty())
        assertTrue(loginResponse.refreshToken.isNotEmpty())
        assertEquals(userResponse.id, loginResponse.user.id)
    }

    @Test
    fun testRegisterAndRefresh() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)
        assertNotNull(userResponse)

        val loginResponse = userRepository.login(params.email, params.password)
        assertNotNull(loginResponse)

        assertTrue(loginResponse.refreshToken.isNotEmpty())

        val refreshResponse = userRepository.refresh(RefreshParams(loginResponse.refreshToken, params.email))
        assertNotNull(refreshResponse)
        assertTrue(refreshResponse.refreshToken.isNotEmpty())
    }

    @Test
    fun testLoginNonExistentUser() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val loginResponse = userRepository.login(randomEmail(), faker.bigBangTheory.quotes())

        assertNull(loginResponse)
    }

    @Test
    fun testIncorrectLogin(): Unit = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)
        assertNotNull(userResponse)

        val loginResponse = userRepository.login(params.email, faker.device.manufacturer())
        assertNull(loginResponse)
    }

    @Test
    fun testCantRegisterSameUserTwice(): Unit = runBlocking {
        val userRepository = UserRepository(MockUserDataSource(), config)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)
        assertNotNull(userResponse)

        val exception = try {
            userRepository.register(params)
            null
        } catch (e: EmailAlreadyExistsException) {
            e
        }

        assertNotNull(exception)
    }

    private fun randomRegistrationParams(): RegistrationParams {
        return RegistrationParams(randomEmail(),
            faker.name.firstName(),
            faker.name.lastName(),
            faker.cultureSeries.cultureShips())
    }
}
