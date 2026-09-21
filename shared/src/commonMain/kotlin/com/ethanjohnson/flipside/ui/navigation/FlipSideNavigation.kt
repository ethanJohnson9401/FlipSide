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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ethanjohnson.flipside.data.MediaRepository
import com.ethanjohnson.flipside.data.recommendation.LocalMediaRecommendationService
import com.ethanjohnson.flipside.data.recommendation.RecommendationCandidateService
import com.ethanjohnson.flipside.data.search.CachedMediaSearchRepository
import com.ethanjohnson.flipside.data.search.MediaSearchRepository
import com.ethanjohnson.flipside.data.search.MusicBrainzSearchService
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult
import com.ethanjohnson.flipside.screen.add.AddScreen
import com.ethanjohnson.flipside.screen.collection.CollectionScreen
import com.ethanjohnson.flipside.screen.detail.MediaDetailScreen
import com.ethanjohnson.flipside.screen.edit.EditMediaScreen
import com.ethanjohnson.flipside.screen.home.HomeScreen
import com.ethanjohnson.flipside.screen.recommendation.RecommendationDetailScreen
import com.ethanjohnson.flipside.screen.wishlist.WishlistScreen

@Composable
fun FlipSideNavigation(
    mediaRepository: MediaRepository,
    useNavigationRail: Boolean
) {
    val collectionItems by
    mediaRepository
        .collectionItems
        .collectAsState()

    val wishlistItems by
    mediaRepository
        .wishlistItems
        .collectAsState()

    val searchRepository:
            MediaSearchRepository =
        remember {
            CachedMediaSearchRepository(
                database =
                    mediaRepository.database,
                searchService =
                    MusicBrainzSearchService()
            )
        }

    val recommendationService =
        remember {
            LocalMediaRecommendationService()
        }

    val recommendationCandidateService =
        remember(
            searchRepository
        ) {
            RecommendationCandidateService(
                searchRepository =
                    searchRepository
            )
        }

    var currentDestination by remember {
        mutableStateOf(
            FlipSideDestination.HOME
        )
    }

    var selectedMediaItemId by remember {
        mutableStateOf<String?>(null)
    }

    var selectedRecommendation by remember {
        mutableStateOf<MediaSearchResult?>(
            null
        )
    }

    var isEditingMedia by remember {
        mutableStateOf(false)
    }

    val recommendationItems by
    produceState<List<MediaSearchResult>>(
        initialValue =
            emptyList(),
        collectionItems,
        wishlistItems,
        currentDestination
    ) {
        if (
            currentDestination !=
            FlipSideDestination.HOME
        ) {
            value =
                emptyList()

            return@produceState
        }

        if (
            collectionItems.isEmpty()
        ) {
            value =
                emptyList()

            return@produceState
        }

        val candidates =
            recommendationCandidateService
                .discoverCandidates(
                    collection =
                        collectionItems
                )

        value =
            recommendationService
                .recommendations(
                    collection =
                        collectionItems,
                    wishlist =
                        wishlistItems,
                    candidates =
                        candidates
                )
    }

    val onDestinationSelected:
                (FlipSideDestination) -> Unit =
        { destination ->

            currentDestination =
                destination

            selectedMediaItemId =
                null

            selectedRecommendation =
                null

            isEditingMedia =
                false
        }

    val onMediaClick:
                (MediaItem) -> Unit =
        { item ->

            selectedMediaItemId =
                item.id

            selectedRecommendation =
                null

            isEditingMedia =
                false
        }

    val onRecommendationClick:
                (MediaSearchResult) -> Unit =
        { result ->

            selectedRecommendation =
                result

            selectedMediaItemId =
                null

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
                recommendationItems =
                    recommendationItems,
                selectedMediaItemId =
                    selectedMediaItemId,
                selectedRecommendation =
                    selectedRecommendation,
                isEditingMedia =
                    isEditingMedia,
                mediaRepository =
                    mediaRepository,
                searchRepository =
                    searchRepository,
                onMediaClick =
                    onMediaClick,
                onRecommendationClick =
                    onRecommendationClick,
                onRecommendationBack = {
                    selectedRecommendation =
                        null
                },
                onMediaDetailBack =
                    onMediaDetailBack,
                onEditMedia = {
                    isEditingMedia = true
                },
                onEditFinished = {
                    isEditingMedia = false
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
                recommendationItems =
                    recommendationItems,
                selectedMediaItemId =
                    selectedMediaItemId,
                selectedRecommendation =
                    selectedRecommendation,
                isEditingMedia =
                    isEditingMedia,
                mediaRepository =
                    mediaRepository,
                searchRepository =
                    searchRepository,
                onMediaClick =
                    onMediaClick,
                onRecommendationClick =
                    onRecommendationClick,
                onRecommendationBack = {
                    selectedRecommendation =
                        null
                },
                onMediaDetailBack =
                    onMediaDetailBack,
                onEditMedia = {
                    isEditingMedia = true
                },
                onEditFinished = {
                    isEditingMedia = false
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
    recommendationItems:
    List<MediaSearchResult>,
    selectedMediaItemId:
    String?,
    selectedRecommendation:
    MediaSearchResult?,
    isEditingMedia:
    Boolean,
    mediaRepository:
    MediaRepository,
    searchRepository:
    MediaSearchRepository,
    onMediaClick:
        (MediaItem) -> Unit,
    onRecommendationClick:
        (MediaSearchResult) -> Unit,
    onRecommendationBack:
        () -> Unit,
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

        if (
            selectedRecommendation != null
        ) {
            val result =
                selectedRecommendation

            RecommendationDetailScreen(
                result = result,
                onBack =
                    onRecommendationBack,
                onAddToCollection = {
                    val format =
                        result.format

                    if (format != null) {
                        mediaRepository
                            .addToCollection(
                                title =
                                    result.title,
                                subtitle =
                                    result.subtitle,
                                format =
                                    format,
                                year =
                                    result.year,
                                edition =
                                    result.edition,
                                condition =
                                    null,
                                purchasePrice =
                                    null,
                                notes =
                                    null,
                                coverArtUrl =
                                    result.coverArtUrl
                            )

                        onRecommendationBack()
                    }
                },
                onAddToWishlist = {
                    val format =
                        result.format

                    if (format != null) {
                        mediaRepository
                            .addToWishlist(
                                title =
                                    result.title,
                                subtitle =
                                    result.subtitle,
                                format =
                                    format,
                                year =
                                    result.year,
                                edition =
                                    result.edition,
                                condition =
                                    null,
                                notes =
                                    null,
                                coverArtUrl =
                                    result.coverArtUrl
                            )

                        onRecommendationBack()
                    }
                }
            )

            return@Box
        }

        when (currentDestination) {
            FlipSideDestination.HOME -> {
                HomeScreen(
                    collectionItems =
                        collectionItems,
                    recommendationItems =
                        recommendationItems,
                    onMediaClick =
                        onMediaClick,
                    onRecommendationClick =
                        onRecommendationClick
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
                    searchRepository =
                        searchRepository,

                    onAddToCollection = {
                            title,
                            subtitle,
                            format,
                            year,
                            edition,
                            condition,
                            purchasePrice,
                            notes,
                            coverArtUrl ->

                        mediaRepository
                            .addToCollection(
                                title = title,
                                subtitle = subtitle,
                                format = format,
                                year = year,
                                edition = edition,
                                condition = condition,
                                purchasePrice =
                                    purchasePrice,
                                notes = notes,
                                coverArtUrl =
                                    coverArtUrl
                            )
                    },

                    onAddToWishlist = {
                            title,
                            subtitle,
                            format,
                            year,
                            edition,
                            condition,
                            notes,
                            coverArtUrl ->

                        mediaRepository
                            .addToWishlist(
                                title = title,
                                subtitle = subtitle,
                                format = format,
                                year = year,
                                edition = edition,
                                condition = condition,
                                notes = notes,
                                coverArtUrl =
                                    coverArtUrl
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
            Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "Discover"
        )
    }
}