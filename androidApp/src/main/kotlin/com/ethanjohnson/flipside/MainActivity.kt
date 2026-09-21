package com.ethanjohnson.flipside

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.data.database.AndroidSqlDriverFactory
import com.ethanjohnson.flipside.data.database.createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val mediaRepository = remember {
                MediaRepository(
                    createDatabase(
                        AndroidSqlDriverFactory(applicationContext)
                    )
                )
            }

            App(
                mediaRepository = mediaRepository
            )
        }
    }
}