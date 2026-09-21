package com.ethanjohnson.flipside.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult
import com.ethanjohnson.flipside.ui.components.MediaCard
import com.ethanjohnson.flipside.ui.theme.DustyRed

@Composable
fun HomeScreen(
    collectionItems: List<MediaItem>,
    recommendationItems: List<MediaSearchResult>,
    onMediaClick: (MediaItem) -> Unit,
    onRecommendationClick:
        (MediaSearchResult) -> Unit
) {
    val recentItems =
        collectionItems.take(5)

    BoxWithConstraints(
        modifier =
            Modifier.fillMaxSize()
    ) {
        val isWideLayout =
            maxWidth >= 900.dp

        val horizontalPadding =
            if (isWideLayout) {
                32.dp
            } else {
                16.dp
            }

        Box(
            modifier =
                Modifier.fillMaxSize(),
            contentAlignment =
                Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(
                        max = 1200.dp
                    )
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal =
                            horizontalPadding
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(
                        24.dp
                    )
            ) {
                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )

                HomeHeader()

                CollectionStats(
                    collectionItems =
                        collectionItems
                )

                MediaCollectionSection(
                    title =
                        "Recently Added",
                    items =
                        recentItems,
                    isWideLayout =
                        isWideLayout,
                    onMediaClick =
                        onMediaClick
                )

                RecommendationSection(
                    recommendations =
                        recommendationItems,
                    isWideLayout =
                        isWideLayout,
                    onRecommendationClick =
                        onRecommendationClick
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            32.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                6.dp
            )
    ) {
        Text(
            text = "FlipSide",
            style =
                MaterialTheme
                    .typography
                    .displayMedium
        )

        Text(
            text =
                "Your physical media, all in one place.",
            style =
                MaterialTheme
                    .typography
                    .bodyLarge,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Card(
            modifier = Modifier
                .width(56.dp)
                .height(4.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        DustyRed
                )
        ) {}
    }
}

@Composable
private fun CollectionStats(
    collectionItems:
    List<MediaItem>
) {
    val vinylCount =
        collectionItems.count {
            it.format ==
                    MediaFormat.VINYL
        }

    val cdCount =
        collectionItems.count {
            it.format ==
                    MediaFormat.CD
        }

    val videoCount =
        collectionItems.count {
            it.format ==
                    MediaFormat.VHS ||
                    it.format ==
                    MediaFormat.DVD ||
                    it.format ==
                    MediaFormat.BLURAY ||
                    it.format ==
                    MediaFormat.UHD_BLURAY
        }

    val gameCount =
        collectionItems.count {
            it.format ==
                    MediaFormat.GAME
        }

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        Text(
            text = "Your Collection",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        FlowRow(
            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {
            StatCard(
                "Total",
                collectionItems.size
            )

            StatCard(
                "Vinyl",
                vinylCount
            )

            StatCard(
                "CD",
                cdCount
            )

            StatCard(
                "Video",
                videoCount
            )

            StatCard(
                "Games",
                gameCount
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    count: Int
) {
    Card(
        modifier =
            Modifier.widthIn(
                min = 130.dp
            ),
        shape =
            MaterialTheme.shapes.large
    ) {
        Column(
            modifier =
                Modifier.padding(
                    16.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    4.dp
                )
        ) {
            Text(
                text =
                    count.toString(),
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Text(
                text = label,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MediaCollectionSection(
    title: String,
    items: List<MediaItem>,
    isWideLayout: Boolean,
    onMediaClick:
        (MediaItem) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        Text(
            text = title,
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        if (items.isEmpty()) {
            Text(
                text =
                    "Nothing here yet. Add some media to get started.",
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            return
        }

        if (isWideLayout) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        16.dp
                    )
            ) {
                items.forEach { item ->
                    MediaCard(
                        title =
                            item.title,
                        subtitle =
                            item.subtitle,
                        format =
                            item.format
                                .displayName,
                        coverArtUrl =
                            item.coverArtUrl,
                        modifier =
                            Modifier.weight(
                                1f
                            ),
                        onClick = {
                            onMediaClick(
                                item
                            )
                        }
                    )
                }

                repeat(
                    5 - items.size
                ) {
                    Spacer(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        14.dp
                    )
            ) {
                items(
                    items,
                    key = {
                        it.id
                    }
                ) { item ->
                    MediaCard(
                        title =
                            item.title,
                        subtitle =
                            item.subtitle,
                        format =
                            item.format
                                .displayName,
                        coverArtUrl =
                            item.coverArtUrl,
                        modifier =
                            Modifier.width(
                                170.dp
                            ),
                        onClick = {
                            onMediaClick(
                                item
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationSection(
    recommendations:
    List<MediaSearchResult>,
    isWideLayout: Boolean,
    onRecommendationClick:
        (MediaSearchResult) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        Text(
            text = "Picked For You",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        if (
            recommendations.isEmpty()
        ) {
            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier =
                        Modifier.padding(
                            18.dp
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(
                            5.dp
                        )
                ) {
                    Text(
                        text =
                            "Recommendations are learning your taste.",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Text(
                        text =
                            "Add some media to help FlipSide find releases for you.",
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )
                }
            }

            return
        }

        val visible =
            recommendations.take(5)

        if (isWideLayout) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        16.dp
                    )
            ) {
                visible.forEach {
                        result ->

                    RecommendationCard(
                        result =
                            result,
                        modifier =
                            Modifier.weight(
                                1f
                            ),
                        onClick = {
                            onRecommendationClick(
                                result
                            )
                        }
                    )
                }

                repeat(
                    5 - visible.size
                ) {
                    Spacer(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        14.dp
                    )
            ) {
                items(
                    visible,
                    key = {
                        "${it.source}:${it.externalId}"
                    }
                ) { result ->
                    RecommendationCard(
                        result =
                            result,
                        modifier =
                            Modifier.width(
                                170.dp
                            ),
                        onClick = {
                            onRecommendationClick(
                                result
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    result: MediaSearchResult,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    MediaCard(
        title =
            result.title,
        subtitle =
            result.subtitle,
        format =
            result.format
                ?.displayName
                ?: "Unknown",
        coverArtUrl =
            result.coverArtUrl,
        modifier =
            modifier,
        onClick =
            onClick
    )
}