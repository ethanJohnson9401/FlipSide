package com.ethanjohnson.flipside

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.ui.navigation.FlipSideNavigation
import com.ethanjohnson.flipside.ui.theme.FlipSideTheme

@Composable
fun App(
    mediaRepository: MediaRepository
) {
    FlipSideTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FlipSideNavigation(
                mediaRepository = mediaRepository
            )
        }
    }
}