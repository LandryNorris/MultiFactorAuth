package com.landry.multifactor.unit

import com.landry.multifactor.base64Encoded
import com.landry.multifactor.datasource.MockUserDataSource
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import com.landry.multifactor.faker
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.randomEmail
import com.landry.multifactor.repos.UserRepository
import com.landry.multifactor.utils.EncryptionHelper
import io.ktor.config.ApplicationConfig
import io.ktor.config.MapApplicationConfig
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            startKoin()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            stopKoin()
        }

        private fun startKoin() {
            val module = module {
                single<ApplicationConfig> {
                    MapApplicationConfig(
                        "encryption.strongKey" to EncryptionHelper.generateKey().base64Encoded(),
                        "jwt.secret" to "abcdefg",
                        "jwt.audience" to "jwt audience",
                        "jwt.realm" to "server",
                        "jwt.domain" to "https://jwt-provider-domain/"
                    )
                }
            }
            org.koin.core.context.startKoin {
                modules(module)
            }
        }
    }

    @Test
    fun testRegisterUser() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource())
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
        val userRepository = UserRepository(userDataSource)
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)

        assertNotNull(userResponse)
        val storedUser = userDataSource.getRawUsersList().first()
        assertTrue(storedUser.iv.isNotEmpty())
        assertEquals(params.email, storedUser.email) //email is stored in plain text for searching.
        assertNotEquals(params.firstName, storedUser.firstName)
        assertNotEquals(params.lastName, storedUser.lastName)

        val decryptedUser = storedUser.decrypt()

        assertEquals(params.email, decryptedUser.email)
        assertEquals(params.firstName, decryptedUser.firstName)
        assertEquals(params.lastName, decryptedUser.lastName)
    }

    @Test
    fun testRegisterAndLogin() = runBlocking {
        val userRepository = UserRepository(MockUserDataSource())
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
        val userRepository = UserRepository(MockUserDataSource())
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
        val userRepository = UserRepository(MockUserDataSource())
        val loginResponse = userRepository.login(randomEmail(), faker.bigBangTheory.quotes())

        assertNull(loginResponse)
    }

    @Test
    fun testIncorrectLogin(): Unit = runBlocking {
        val userRepository = UserRepository(MockUserDataSource())
        val params = randomRegistrationParams()
        val userResponse = userRepository.register(params)
        assertNotNull(userResponse)

        val loginResponse = userRepository.login(params.email, faker.device.manufacturer())
        assertNull(loginResponse)
    }

    @Test
    fun testCantRegisterSameUserTwice(): Unit = runBlocking {
        val userRepository = UserRepository(MockUserDataSource())
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
        return RegistrationParams(randomEmail(), faker.name.firstName(), faker.name.lastName(), faker.cultureSeries.cultureShips())
    }
}
