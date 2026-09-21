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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.ui.components.CollectionStatCard
import com.ethanjohnson.flipside.ui.components.MediaCard
import com.ethanjohnson.flipside.ui.components.SectionHeader

private data class DemoMediaItem(
    val title: String,
    val subtitle: String,
    val format: String
)

@Composable
fun HomeScreen() {
    val recentItems = listOf(
        DemoMediaItem(
            title = "The Dark Side of the Moon",
            subtitle = "Pink Floyd • 1973",
            format = "Vinyl"
        ),
        DemoMediaItem(
            title = "Blade Runner",
            subtitle = "1982",
            format = "VHS"
        ),
        DemoMediaItem(
            title = "Super Mario World",
            subtitle = "1991",
            format = "Game"
        ),
        DemoMediaItem(
            title = "Abbey Road",
            subtitle = "The Beatles • 1969",
            format = "Vinyl"
        ),
        DemoMediaItem(
            title = "The Thing",
            subtitle = "1982",
            format = "VHS"
        )
    )

    val recommendations = listOf(
        DemoMediaItem(
            title = "Wish You Were Here",
            subtitle = "Pink Floyd • 1975",
            format = "Vinyl"
        ),
        DemoMediaItem(
            title = "Alien",
            subtitle = "1979",
            format = "VHS"
        ),
        DemoMediaItem(
            title = "Chrono Trigger",
            subtitle = "1995",
            format = "Game"
        ),
        DemoMediaItem(
            title = "Rumours",
            subtitle = "Fleetwood Mac • 1977",
            format = "Vinyl"
        ),
        DemoMediaItem(
            title = "Metroid Prime",
            subtitle = "2002",
            format = "Game"
        )
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideLayout = maxWidth >= 900.dp
        val horizontalPadding = if (isWideLayout) 32.dp else 16.dp
        val mediaCardWidth = if (isWideLayout) 190.dp else 170.dp

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 1200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "FlipSide",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "Your collection, rediscovered.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                        )
                    }
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        SectionHeader(title = "Your Collection")

                        if (isWideLayout) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CollectionStatCard(
                                    label = "Vinyl",
                                    count = 42,
                                    modifier = Modifier.weight(1f)
                                )

                                CollectionStatCard(
                                    label = "CDs",
                                    count = 68,
                                    modifier = Modifier.weight(1f)
                                )

                                CollectionStatCard(
                                    label = "VHS",
                                    count = 17,
                                    modifier = Modifier.weight(1f)
                                )

                                CollectionStatCard(
                                    label = "Games",
                                    count = 31,
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
                                        count = 42,
                                        modifier = Modifier.weight(1f)
                                    )

                                    CollectionStatCard(
                                        label = "CDs",
                                        count = 68,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CollectionStatCard(
                                        label = "VHS",
                                        count = 17,
                                        modifier = Modifier.weight(1f)
                                    )

                                    CollectionStatCard(
                                        label = "Games",
                                        count = 31,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    MediaSection(
                        title = "Recently Added",
                        actionText = "See All",
                        items = recentItems,
                        cardWidth = mediaCardWidth
                    )
                }

                item {
                    MediaSection(
                        title = "Picked For You",
                        actionText = "Explore",
                        items = recommendations,
                        cardWidth = mediaCardWidth
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun MediaSection(
    title: String,
    actionText: String,
    items: List<DemoMediaItem>,
    cardWidth: androidx.compose.ui.unit.Dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            title = title,
            actionText = actionText
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items) { item ->
                MediaCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    format = item.format,
                    modifier = Modifier.width(cardWidth)
                )
            }
        }
    }
}