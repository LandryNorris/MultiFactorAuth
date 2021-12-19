package com.landry.multifactor

import dev.gitlive.firebase.AdminFirebaseOptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import io.ktor.config.*

val firebaseApp by lazy {
    Firebase.initialize(Unit, AdminFirebaseOptions(config.property("firebase.jsonPath").getString()))
}

val config by inject<ApplicationConfig>()
