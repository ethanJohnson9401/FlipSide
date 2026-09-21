package com.ethanjohnson.flipside

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.data.database.IosSqlDriverFactory
import com.ethanjohnson.flipside.data.database.createDatabase

fun MainViewController() = ComposeUIViewController {
    val mediaRepository = remember {
        MediaRepository(
            createDatabase(
                IosSqlDriverFactory()
            )
        )
    }

    App(
        mediaRepository = mediaRepository
    )
}