package com.ethanjohnson.flipside.data.database

import app.cash.sqldelight.db.SqlDriver

interface SqlDriverFactory {
    fun createDriver(): SqlDriver
}