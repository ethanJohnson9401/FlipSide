package com.ethanjohnson.flipside.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ethanjohnson.flipside.db.FlipSideDatabase

class AndroidSqlDriverFactory(
    private val context: Context
) : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = FlipSideDatabase.Schema,
            context = context,
            name = "flipside.db"
        )
    }
}