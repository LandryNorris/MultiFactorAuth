package com.landry.shared.sql

import com.landry.multifactor.OtpDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(OtpDatabase.Schema, "otp.db")
}