package funny.buildapp.common.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import funny.buildapp.Database

public actual class DriverFactory(private val context: Context) {
    public actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, "worklite.db")
    }
}