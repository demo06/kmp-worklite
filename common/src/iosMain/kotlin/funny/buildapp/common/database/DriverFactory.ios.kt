package funny.buildapp.common.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import funny.buildapp.Database

public actual class DriverFactory {
    public actual fun createDriver(): SqlDriver {
            return NativeSqliteDriver(Database.Schema, "worklite.db")
    }
}