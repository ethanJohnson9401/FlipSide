package com.ethanjohnson.flipside

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.ui.navigation.FlipSideNavigation
import com.ethanjohnson.flipside.ui.theme.FlipSideTheme

@Composable
fun App(
    mediaRepository: MediaRepository
) {
    FlipSideTheme {
        BoxWithConstraints {
            val useNavigationRail =
                maxWidth >= 840.dp

            FlipSideNavigation(
                mediaRepository =
                    mediaRepository,
                useNavigationRail =
                    useNavigationRail
            )
        }
    }
}