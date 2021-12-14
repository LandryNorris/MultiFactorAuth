package com.landry.shared.sql

import com.landry.multifactor.Otp
import com.landry.multifactor.OtpDatabase
import com.landry.shared.asValue
import com.squareup.sqldelight.runtime.coroutines.asFlow

class OtpDatabaseDataSource(db: OtpDatabase) {
    private val queries = db.otpQueries

    /**
     * Get all Otp entries in the DB.
     */
    fun getAll(): List<Otp> = queries.selectAllOtp().executeAsList()

    /**
     * Get all Otp entries in the DB as a flow.
     */
    fun getAllAsFlow() = queries.selectAllOtp().asFlow()

    fun getAllAsValue() = queries.selectAllOtp().asValue()

    /**
     * Get the first DB entry with the given ID or null if none exist.
     */
    fun getByIdOrNull(id: Long) = queries.getById(id).executeAsOneOrNull()

    /**
     * Insert a new Otp object into the database. The id field is always ignored.
     */
    fun insert(otp: Otp) = otp.run { queries.insert(null, name, secret, counter) }

    /**
     * Update the secret for the row with the given id.
     */
    fun updateSecretById(id: Long, secret: String) = queries.updateSecretById(secret, id)
}