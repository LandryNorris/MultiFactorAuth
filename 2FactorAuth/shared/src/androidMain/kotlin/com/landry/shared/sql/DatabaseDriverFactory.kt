package com.landry.shared.sql

import android.content.Context
import com.landry.multifactor.OtpDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(OtpDatabase.Schema, context, "otp.db")
}