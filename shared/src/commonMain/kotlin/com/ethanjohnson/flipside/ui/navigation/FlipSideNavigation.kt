package com.ethanjohnson.flipside.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.screen.add.AddScreen
import com.ethanjohnson.flipside.screen.collection.CollectionScreen
import com.ethanjohnson.flipside.screen.detail.MediaDetailScreen
import com.ethanjohnson.flipside.screen.edit.EditMediaScreen
import com.ethanjohnson.flipside.screen.home.HomeScreen
import com.ethanjohnson.flipside.screen.wishlist.WishlistScreen

@Composable
fun FlipSideNavigation(
    mediaRepository: MediaRepository,
    useNavigationRail: Boolean
) {
    val collectionItems by mediaRepository
        .collectionItems
        .collectAsState()

    val wishlistItems by mediaRepository
        .wishlistItems
        .collectAsState()

    var currentDestination by remember {
        mutableStateOf(
            FlipSideDestination.HOME
        )
    }

    var selectedMediaItemId by remember {
        mutableStateOf<String?>(null)
    }

    var isEditingMedia by remember {
        mutableStateOf(false)
    }

    val onDestinationSelected:
                (FlipSideDestination) -> Unit =
        { destination ->

            currentDestination =
                destination

            selectedMediaItemId =
                null

            isEditingMedia =
                false
        }

    val onMediaClick:
                (MediaItem) -> Unit =
        { item ->

            selectedMediaItemId =
                item.id

            isEditingMedia =
                false
        }

    val onMediaDetailBack:
                () -> Unit = {

        selectedMediaItemId =
            null

        isEditingMedia =
            false
    }

    if (useNavigationRail) {
        Row(
            modifier =
                Modifier.fillMaxSize()
        ) {
            FlipSideNavRail(
                currentDestination =
                    currentDestination,
                onDestinationSelected =
                    onDestinationSelected
            )

            FlipSideContent(
                modifier =
                    Modifier.weight(1f),
                currentDestination =
                    currentDestination,
                collectionItems =
                    collectionItems,
                wishlistItems =
                    wishlistItems,
                selectedMediaItemId =
                    selectedMediaItemId,
                isEditingMedia =
                    isEditingMedia,
                mediaRepository =
                    mediaRepository,
                onMediaClick =
                    onMediaClick,
                onMediaDetailBack =
                    onMediaDetailBack,
                onEditMedia = {
                    isEditingMedia =
                        true
                },
                onEditFinished = {
                    isEditingMedia =
                        false
                }
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                FlipSideNavBar(
                    currentDestination =
                        currentDestination,
                    onDestinationSelected =
                        onDestinationSelected
                )
            }
        ) { innerPadding ->

            FlipSideContent(
                modifier =
                    Modifier.fillMaxSize(),
                currentDestination =
                    currentDestination,
                collectionItems =
                    collectionItems,
                wishlistItems =
                    wishlistItems,
                selectedMediaItemId =
                    selectedMediaItemId,
                isEditingMedia =
                    isEditingMedia,
                mediaRepository =
                    mediaRepository,
                onMediaClick =
                    onMediaClick,
                onMediaDetailBack =
                    onMediaDetailBack,
                onEditMedia = {
                    isEditingMedia =
                        true
                },
                onEditFinished = {
                    isEditingMedia =
                        false
                },
                contentPadding =
                    innerPadding
            )
        }
    }
}

@Composable
private fun FlipSideContent(
    currentDestination:
    FlipSideDestination,
    collectionItems:
    List<MediaItem>,
    wishlistItems:
    List<MediaItem>,
    selectedMediaItemId:
    String?,
    isEditingMedia:
    Boolean,
    mediaRepository:
    MediaRepository,
    onMediaClick:
        (MediaItem) -> Unit,
    onMediaDetailBack:
        () -> Unit,
    onEditMedia:
        () -> Unit,
    onEditFinished:
        () -> Unit,
    modifier:
    Modifier = Modifier,
    contentPadding:
    PaddingValues =
        PaddingValues()
) {
    val selectedMediaItem =
        selectedMediaItemId?.let { id ->

            collectionItems
                .firstOrNull {
                    it.id == id
                }
                ?: wishlistItems
                    .firstOrNull {
                        it.id == id
                    }
        }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                contentPadding
            )
    ) {
        if (
            selectedMediaItem != null &&
            isEditingMedia
        ) {
            EditMediaScreen(
                item =
                    selectedMediaItem,
                onCancel = {
                    onEditFinished()
                },
                onSave = {
                        updatedItem ->

                    mediaRepository
                        .updateMediaItem(
                            updatedItem
                        )

                    onEditFinished()
                }
            )

            return@Box
        }

        if (
            selectedMediaItem != null
        ) {
            MediaDetailScreen(
                item =
                    selectedMediaItem,
                onBack = {
                    onEditFinished()
                    onMediaDetailBack()
                },
                onEdit = {
                    onEditMedia()
                },
                onDelete = {
                    mediaRepository
                        .deleteMediaItem(
                            selectedMediaItem.id
                        )

                    onEditFinished()
                    onMediaDetailBack()
                },
                onOwnedChange = {
                        owned ->

                    mediaRepository
                        .setOwned(
                            id =
                                selectedMediaItem.id,
                            owned =
                                owned
                        )
                },
                onWishlistChange = {
                        wishlisted ->

                    mediaRepository
                        .setWishlisted(
                            id =
                                selectedMediaItem.id,
                            wishlisted =
                                wishlisted
                        )
                },
                onMoveToCollection = {
                    mediaRepository
                        .moveWishlistItemToCollection(
                            selectedMediaItem
                        )
                }
            )

            return@Box
        }

        when (
            currentDestination
        ) {
            FlipSideDestination.HOME -> {
                HomeScreen(
                    collectionItems =
                        collectionItems,
                    onMediaClick =
                        onMediaClick
                )
            }

            FlipSideDestination.COLLECTION -> {
                CollectionScreen(
                    collectionItems =
                        collectionItems,
                    onMediaClick =
                        onMediaClick
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

                        mediaRepository
                            .addToCollection(
                                title = title,
                                subtitle =
                                    subtitle,
                                format =
                                    format,
                                year =
                                    year,
                                edition =
                                    edition,
                                condition =
                                    condition,
                                purchasePrice =
                                    purchasePrice,
                                notes =
                                    notes
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

                        mediaRepository
                            .addToWishlist(
                                title = title,
                                subtitle =
                                    subtitle,
                                format =
                                    format,
                                year =
                                    year,
                                edition =
                                    edition,
                                condition =
                                    condition,
                                notes =
                                    notes
                            )
                    }
                )
            }

            FlipSideDestination.WISHLIST -> {
                WishlistScreen(
                    wishlistItems =
                        wishlistItems,
                    onMediaClick =
                        onMediaClick
                )
            }

            FlipSideDestination.DISCOVER -> {
                DiscoverPlaceholder()
            }
        }
    }
}

@Composable
private fun DiscoverPlaceholder() {
    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment =
            androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "Discover"
        )
    }
}