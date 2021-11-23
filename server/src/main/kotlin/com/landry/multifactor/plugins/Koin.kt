package com.landry.multifactor.plugins

import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.datasource.AbstractUsersDataSource
import com.landry.multifactor.datasource.FirebaseDeviceDataSource
import com.landry.multifactor.datasource.FirebaseUserDataSource
import com.landry.multifactor.repos.DeviceRepository
import com.landry.multifactor.repos.UserRepository
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    val mainModule = module {
        single { environment.config }

        single<AbstractUsersDataSource> { FirebaseUserDataSource() }
        single { UserRepository(get()) }

        single<AbstractDeviceDataSource> { FirebaseDeviceDataSource() }
        single { DeviceRepository(get()) }
    }
    install(Koin) {
        modules(mainModule)
    }
}