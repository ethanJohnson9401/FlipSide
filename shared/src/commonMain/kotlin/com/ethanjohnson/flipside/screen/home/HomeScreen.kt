package com.ethanjohnson.flipside.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.data.FakeMediaData
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.ui.components.CollectionStatCard
import com.ethanjohnson.flipside.ui.components.MediaCard
import com.ethanjohnson.flipside.ui.components.SectionHeader
import com.ethanjohnson.flipside.model.MediaFormat

@Composable
fun HomeScreen(
    onMediaClick: (MediaItem) -> Unit = {}
) {
    val recentItems = FakeMediaData.recentlyAdded
    val recommendationItems = FakeMediaData.recommendations

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideLayout = maxWidth >= 900.dp

        val horizontalPadding = if (isWideLayout) {
            32.dp
        } else {
            16.dp
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 1200.dp)
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
            ) {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HomeHeader()

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                CollectionStats(
                    isWideLayout = isWideLayout
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                MediaSection(
                    title = "Recently Added",
                    mediaItems = recentItems,
                    isWideLayout = isWideLayout,
                    onMediaClick = onMediaClick
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                MediaSection(
                    title = "Picked For You",
                    mediaItems = recommendationItems,
                    isWideLayout = isWideLayout,
                    onMediaClick = onMediaClick
                )

                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column {
        Text(
            text = "FlipSide",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Your collection, rediscovered.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Box(
            modifier = Modifier
                .width(56.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}

@Composable
private fun CollectionStats(
    isWideLayout: Boolean
) {
    val collectionItems = FakeMediaData.collectionItems

    val vinylCount = collectionItems.count {
        it.format == MediaFormat.VINYL
    }

    val cdCount = collectionItems.count {
        it.format == MediaFormat.CD
    }

    val vhsCount = collectionItems.count {
        it.format == MediaFormat.VHS
    }

    val gameCount = collectionItems.count {
        it.format == MediaFormat.GAME
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Your Collection",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (isWideLayout) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CollectionStatCard(
                    label = "Vinyl",
                    count = vinylCount,
                    modifier = Modifier.weight(1f)
                )

                CollectionStatCard(
                    label = "CDs",
                    count = cdCount,
                    modifier = Modifier.weight(1f)
                )

                CollectionStatCard(
                    label = "VHS",
                    count = vhsCount,
                    modifier = Modifier.weight(1f)
                )

                CollectionStatCard(
                    label = "Games",
                    count = gameCount,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CollectionStatCard(
                        label = "Vinyl",
                        count = vinylCount,
                        modifier = Modifier.weight(1f)
                    )

                    CollectionStatCard(
                        label = "CDs",
                        count = cdCount,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CollectionStatCard(
                        label = "VHS",
                        count = vhsCount,
                        modifier = Modifier.weight(1f)
                    )

                    CollectionStatCard(
                        label = "Games",
                        count = gameCount,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaSection(
    title: String,
    mediaItems: List<MediaItem>,
    isWideLayout: Boolean,
    onMediaClick: (MediaItem) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            title = title
        )

        if (isWideLayout) {
            DesktopMediaRow(
                mediaItems = mediaItems,
                onMediaClick = onMediaClick
            )
        } else {
            MobileMediaRow(
                mediaItems = mediaItems,
                onMediaClick = onMediaClick
            )
        }
    }
}

@Composable
private fun DesktopMediaRow(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        mediaItems.take(5).forEach { item ->
            MediaCard(
                title = item.title,
                subtitle = mediaSubtitle(item),
                format = item.format.displayName,
                modifier = Modifier.weight(1f),
                onClick = {
                    onMediaClick(item)
                }
            )
        }
    }
}

@Composable
private fun MobileMediaRow(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = mediaItems,
            key = { item ->
                item.id
            }
        ) { item ->
            MediaCard(
                title = item.title,
                subtitle = mediaSubtitle(item),
                format = item.format.displayName,
                modifier = Modifier.width(170.dp),
                onClick = {
                    onMediaClick(item)
                }
            )
        }
    }
}

private fun mediaSubtitle(
    item: MediaItem
): String {
    return buildString {
        append(item.subtitle)

        item.year?.let { year ->
            append(" • ")
            append(year)
        }
    }
}