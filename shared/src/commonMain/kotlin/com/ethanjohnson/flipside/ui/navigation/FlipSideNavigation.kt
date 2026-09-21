package com.ethanjohnson.flipside.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.screen.add.AddScreen
import com.ethanjohnson.flipside.screen.collection.CollectionScreen
import com.ethanjohnson.flipside.screen.discover.DiscoverScreen
import com.ethanjohnson.flipside.screen.home.HomeScreen
import com.ethanjohnson.flipside.screen.wishlist.WishlistScreen

@Composable
fun FlipSideNavigation() {
    var currentDestination by remember {
        mutableStateOf(FlipSideDestination.HOME)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val useNavigationRail = maxWidth >= 840.dp

        if (useNavigationRail) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                FlipSideNavigationRail(
                    currentDestination = currentDestination,
                    onDestinationSelected = { destination ->
                        currentDestination = destination
                    }
                )

                Surface(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlipSideDestinationContent(
                        destination = currentDestination
                    )
                }
            }
        } else {
            Scaffold(
                bottomBar = {
                    FlipSideBottomBar(
                        currentDestination = currentDestination,
                        onDestinationSelected = { destination ->
                            currentDestination = destination
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FlipSideDestinationContent(
                        destination = currentDestination
                    )
                }
            }
        }
    }
}

@Composable
private fun FlipSideDestinationContent(
    destination: FlipSideDestination
) {
    when (destination) {
        FlipSideDestination.HOME -> HomeScreen()
        FlipSideDestination.COLLECTION -> CollectionScreen()
        FlipSideDestination.ADD -> AddScreen()
        FlipSideDestination.WISHLIST -> WishlistScreen()
        FlipSideDestination.DISCOVER -> DiscoverScreen()
    }
}