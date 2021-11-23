package com.landry.multifactor.repos

import com.landry.multifactor.datasource.AbstractUsersDataSource
import com.landry.multifactor.datasource.FirebaseUserDataSource
import com.landry.multifactor.exceptions.EmailAlreadyExistsException
import com.landry.multifactor.models.LoginResponse
import com.landry.multifactor.models.UserResponse
import com.landry.multifactor.models.toUserResponse
import com.landry.multifactor.params.RegistrationParams
import com.landry.multifactor.utils.TokenGenerator
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import de.mkammerer.argon2.Argon2Helper

class UserRepository(private val dataSource: AbstractUsersDataSource) {
    private val argon2: Argon2 by lazy { Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id) }

    suspend fun login(email: String, password: String): LoginResponse? {
        val userDoc = dataSource.getUserByEmail(email) ?: return null
        val passwordVerified = argon2.verify(userDoc.passwordHash, password.toCharArray())
        if(!passwordVerified) return null
        val user = userDoc.toUserResponse()

        return LoginResponse(user.email, TokenGenerator.generate(user.email))
    }

    suspend fun register(registrationParams: RegistrationParams): UserResponse {
        val numIterations = Argon2Helper.findIterations(argon2,
            FirebaseUserDataSource.MAX_HASH_TIME,
            FirebaseUserDataSource.HASH_MEMORY,
            FirebaseUserDataSource.HASH_PARALLELISM
        )

        return registrationParams.run {
            val existingUser = dataSource.getUserByEmail(email)
            if(existingUser != null) throw EmailAlreadyExistsException(email)

            val hash = argon2.hash(numIterations, FirebaseUserDataSource.HASH_MEMORY, FirebaseUserDataSource.HASH_PARALLELISM, password.toCharArray())
            dataSource.registerUser(email, firstName, lastName, hash)
        }
    }
}