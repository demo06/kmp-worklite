package funny.buildapp.common.database

import app.cash.sqldelight.db.SqlDriver
import funny.buildapp.Database

public expect class DriverFactory {
    public fun createDriver(): SqlDriver
}

public lateinit var database: Database