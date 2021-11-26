package com.landry.multifactor.repos

import com.landry.multifactor.base64Encode
import com.landry.multifactor.datasource.AbstractUsersDataSource
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import com.landry.multifactor.models.User
import com.landry.multifactor.responses.LoginResponse
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.responses.UserResponse
import com.landry.multifactor.responses.toUserResponse
import com.landry.multifactor.utils.EncryptionHelper
import com.landry.multifactor.utils.TokenGenerator
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

class UserRepository(private val dataSource: AbstractUsersDataSource) {
    companion object {
        val argon2: Argon2 by lazy { Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id) }

        const val HASH_MEMORY = 1024 //kiB
        const val HASH_PARALLELISM = 2

        const val numIterations = 100
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        val user = getUserByEmail(email) ?: return null
        val decryptedPasswordHash = user.decryptPasswordHash()
        val passwordVerified = argon2.verify(decryptedPasswordHash, password.toCharArray())
        if(!passwordVerified) return null

        return LoginResponse(user.email, TokenGenerator.generate(user.email))
    }

    suspend fun register(registrationParams: RegistrationParams): UserResponse {
        registrationParams.run {
            val existingUser = dataSource.userExists(email)
            if(existingUser) throw EmailAlreadyExistsException(email)
            println("Hashing password. $numIterations")

            val hash = argon2.hash(numIterations, HASH_MEMORY, HASH_PARALLELISM, password.toCharArray())
            val iv = EncryptionHelper.generateIV().base64Encode()
            val user = User("", email, firstName, lastName, hash, iv)
            val userResponse = dataSource.registerUser(user.encrypt())
            user.id = userResponse.id

            return user.toUserResponse()
        }
    }

    suspend fun getUserByEmail(email: String) = dataSource.getUserByEmail(email)
}