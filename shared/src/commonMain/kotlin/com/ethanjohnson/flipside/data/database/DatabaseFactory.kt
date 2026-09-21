package com.ethanjohnson.flipside.data.database

import com.ethanjohnson.flipside.db.FlipSideDatabase

fun createDatabase(
    driverFactory: SqlDriverFactory
): FlipSideDatabase {
    return FlipSideDatabase(
        driver = driverFactory.createDriver()
    )
}