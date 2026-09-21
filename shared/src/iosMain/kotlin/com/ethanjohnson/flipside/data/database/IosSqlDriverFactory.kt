package com.ethanjohnson.flipside.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.ethanjohnson.flipside.db.FlipSideDatabase

class IosSqlDriverFactory : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = FlipSideDatabase.Schema,
            name = "flipside.db"
        )
    }
}