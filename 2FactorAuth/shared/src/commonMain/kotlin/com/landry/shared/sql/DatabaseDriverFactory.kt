package com.landry.shared.sql

import com.landry.multifactor.OtpDatabase
import com.squareup.sqldelight.db.SqlDriver

class DatabaseUtils {
    companion object {
        lateinit var database: OtpDatabase; private set

        fun initDatabase(driver: SqlDriver) {
            database = OtpDatabase(driver)
        }
    }
}

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}