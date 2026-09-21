package com.ethanjohnson.flipside.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.screen.add.AddScreen
import com.ethanjohnson.flipside.screen.collection.CollectionScreen
import com.ethanjohnson.flipside.screen.detail.MediaDetailScreen
import com.ethanjohnson.flipside.screen.discover.DiscoverScreen
import com.ethanjohnson.flipside.screen.home.HomeScreen
import com.ethanjohnson.flipside.screen.wishlist.WishlistScreen
import com.ethanjohnson.flipside.data.MediaRepository

@Composable
fun FlipSideNavigation() {

    val mediaRepository = remember {
        MediaRepository()
    }

    var currentDestination by remember {
        mutableStateOf(FlipSideDestination.HOME)
    }

    var selectedMediaItem by remember {
        mutableStateOf<MediaItem?>(null)
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
                        selectedMediaItem = null
                    }
                )

                Surface(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlipSideContent(
                        currentDestination = currentDestination,
                        selectedMediaItem = selectedMediaItem,
                        mediaRepository = mediaRepository,
                        onMediaClick = { item ->
                            selectedMediaItem = item
                        },
                        onMediaDetailBack = {
                            selectedMediaItem = null
                        },
                        onDestinationChange = { destination ->
                            currentDestination = destination
                            selectedMediaItem = null
                        }
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
                            selectedMediaItem = null
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    FlipSideContent(
                        currentDestination = currentDestination,
                        selectedMediaItem = selectedMediaItem,
                        mediaRepository = mediaRepository,
                        onMediaClick = { item ->
                            selectedMediaItem = item
                        },
                        onMediaDetailBack = {
                            selectedMediaItem = null
                        },
                        onDestinationChange = { destination ->
                            currentDestination = destination
                            selectedMediaItem = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FlipSideContent(
    currentDestination: FlipSideDestination,
    selectedMediaItem: MediaItem?,
    mediaRepository: MediaRepository,
    onMediaClick: (MediaItem) -> Unit,
    onMediaDetailBack: () -> Unit,
    onDestinationChange: (FlipSideDestination) -> Unit
) {
    if (selectedMediaItem != null) {
        MediaDetailScreen(
            item = selectedMediaItem,
            onBack = onMediaDetailBack
        )

        return
    }

    when (currentDestination) {
        FlipSideDestination.HOME -> {
            HomeScreen(
                collectionItems = mediaRepository.collectionItems,
                onMediaClick = onMediaClick
            )
        }

        FlipSideDestination.COLLECTION -> {
            CollectionScreen(
                collectionItems = mediaRepository.collectionItems,
                onMediaClick = onMediaClick
            )
        }

        FlipSideDestination.ADD -> {
            AddScreen(
                onAddToCollection = {
                        title,
                        subtitle,
                        format,
                        year,
                        edition,
                        condition,
                        purchasePrice,
                        notes ->

                    mediaRepository.addToCollection(
                        title = title,
                        subtitle = subtitle,
                        format = format,
                        year = year,
                        edition = edition,
                        condition = condition,
                        purchasePrice = purchasePrice,
                        notes = notes
                    )

                    onDestinationChange(
                        FlipSideDestination.COLLECTION
                    )
                },
                onAddToWishlist = {
                        title,
                        subtitle,
                        format,
                        year,
                        edition,
                        condition,
                        notes ->

                    mediaRepository.addToWishlist(
                        title = title,
                        subtitle = subtitle,
                        format = format,
                        year = year,
                        edition = edition,
                        condition = condition,
                        notes = notes
                    )

                    onDestinationChange(
                        FlipSideDestination.WISHLIST
                    )
                }
            )
        }

        FlipSideDestination.WISHLIST -> {
            WishlistScreen(
                wishlistItems = mediaRepository.wishlistItems,
                onMediaClick = onMediaClick
            )
        }

        FlipSideDestination.DISCOVER -> {
            DiscoverScreen()
        }
    }
}