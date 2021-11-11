package com.landry.multifactor.plugins

import com.landry.multifactor.datasource.AbstractDeviceDataSource
import com.landry.multifactor.datasource.FirebaseDeviceDataSource
import com.landry.multifactor.module
import com.landry.multifactor.repos.DeviceRepository
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.modules

fun Application.installKoin() {
    val mainModule = module {
        single { environment.config }
        single<AbstractDeviceDataSource> { FirebaseDeviceDataSource() }
        single { DeviceRepository(get()) }
    }
    install(Koin) {
        modules(mainModule)
    }
}