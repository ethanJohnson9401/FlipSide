package com.ethanjohnson.flipside

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.data.database.DesktopSqlDriverFactory
import com.ethanjohnson.flipside.data.database.createDatabase

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FlipSide",
    ) {
        val mediaRepository = remember {
            MediaRepository(
                createDatabase(
                    DesktopSqlDriverFactory()
                )
            )
        }

        App(
            mediaRepository = mediaRepository
        )
    }
}