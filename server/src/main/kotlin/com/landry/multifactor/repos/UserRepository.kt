package com.landry.multifactor.repos

import com.landry.multifactor.base64Encode
import com.landry.multifactor.datasource.AbstractUsersDataSource
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import com.landry.multifactor.models.User
import com.landry.multifactor.params.RefreshParams
import com.landry.multifactor.responses.LoginResponse
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.responses.RefreshResponse
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.responses.toUserResponse
import com.landry.multifactor.utils.EncryptionHelper
import com.landry.multifactor.utils.TokenGenerator
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import io.ktor.http.*

class UserRepository(private val dataSource: AbstractUsersDataSource) {
    companion object {
        val argon2: Argon2 by lazy { Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id) }

        const val HASH_MEMORY = 1024 //kiB
        const val HASH_PARALLELISM = 2

        const val numIterations = 100
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        val user = getUserByEmail(email) ?: return null
        val passwordVerified = argon2.verify(user.passwordHash, password.toCharArray())
        if(!passwordVerified) return null

        val accessToken = TokenGenerator.generate(user.email)
        val refreshToken = TokenGenerator.generateRefresh(user.email)

        return LoginResponse(user.toUserResponse(), accessToken, refreshToken)
    }

    fun refresh(refreshParams: RefreshParams): RefreshResponse {
        val accessToken = TokenGenerator.generate(refreshParams.email)
        val refreshToken = TokenGenerator.generateRefresh(refreshParams.email)

        return RefreshResponse(accessToken, refreshToken)
    }

    suspend fun register(registrationParams: RegistrationParams): UserResponse {
        registrationParams.run {
            val existingUser = dataSource.userExists(email)
            if(existingUser) throw EmailAlreadyExistsException(email)
            println("Hashing password. $numIterations")

            val hash = argon2.hash(numIterations, HASH_MEMORY, HASH_PARALLELISM, password.toCharArray())
            val iv = EncryptionHelper.generateIV().base64Encode()
            val user = User("", email, firstName, lastName, hash, iv, false, false)
            val userResponse = dataSource.registerUser(user.encrypt())
            user.id = userResponse.id

            return user.toUserResponse()
        }
    }

    suspend fun getUserByEmail(email: String) = dataSource.getUserByEmail(email)?.decrypt()
}