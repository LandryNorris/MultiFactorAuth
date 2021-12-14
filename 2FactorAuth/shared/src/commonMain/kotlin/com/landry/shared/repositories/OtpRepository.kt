package com.landry.shared.repositories

import com.landry.shared.sql.DatabaseUtils
import com.landry.shared.sql.OtpDatabaseDataSource

class OtpRepository {
    private val database = DatabaseUtils.database
    private val dataSource = OtpDatabaseDataSource(database)

    fun watchOtp() = dataSource.getAllAsValue()
}