package com.ethanjohnson.flipside.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ethanjohnson.flipside.db.FlipSideDatabase
import java.util.Properties

class DesktopSqlDriverFactory : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = "jdbc:sqlite:flipside.db",
            properties = Properties(),
            schema = FlipSideDatabase.Schema
        )
    }
}