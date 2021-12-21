package com.landry.multifactor.plugins

import com.landry.multifactor.datasource.*
import com.landry.multifactor.repos.DeviceRepository
import com.landry.multifactor.repos.UserRepository
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    val mainModule = module {
        single { environment.config }
        single<AbstractUsersDataSource> { FirebaseUserDataSource() }
        single<AbstractDeviceDataSource> { FirebaseDeviceDataSource() }

        single { UserRepository(get(), get()) }
        single { DeviceRepository(get(), get()) }
    }
    install(Koin) {
        modules(mainModule)
    }
}

fun Application.configureKoinTest() {
    val testModule = module {
        single { environment.config }
        single<AbstractUsersDataSource> { MockUserDataSource() }
        single<AbstractDeviceDataSource> { MockDeviceDataSource() }

        single { UserRepository(get(), get()) }
        single { DeviceRepository(get(), get()) }
    }
    install(Koin) {
        modules(testModule)
    }
}
