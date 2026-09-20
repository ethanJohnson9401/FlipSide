package com.ethanjohnson.flipside.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(12.dp))

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
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionHeader(title = "Your Collection")

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

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionHeader(
                    title = "Recently Added",
                    actionText = "See All"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentItems) { item ->
                        MediaCard(
                            title = item.title,
                            subtitle = item.subtitle,
                            format = item.format,
                            modifier = Modifier.fillParentMaxWidth(0.58f)
                        )
                    }
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionHeader(
                    title = "Picked For You",
                    actionText = "Explore"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recommendations) { item ->
                        MediaCard(
                            title = item.title,
                            subtitle = item.subtitle,
                            format = item.format,
                            modifier = Modifier.fillParentMaxWidth(0.58f)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}