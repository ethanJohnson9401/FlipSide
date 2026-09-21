package com.ethanjohnson.flipside.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ethanjohnson.flipside.db.FlipSideDatabase
import java.io.File
import java.util.Properties

class DesktopSqlDriverFactory : SqlDriverFactory {

    override fun createDriver(): SqlDriver {
        val databaseFile = getDatabaseFile()

        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${databaseFile.absolutePath}",
            properties = Properties(),
            schema = FlipSideDatabase.Schema
        )
    }

    private fun getDatabaseFile(): File {
        val appDirectory = File(
            System.getProperty("user.home"),
            ".flipside"
        )

        if (!appDirectory.exists()) {
            appDirectory.mkdirs()
        }

        return File(
            appDirectory,
            "flipside.db"
        )
    }
}