package com.ethanjohnson.flipside.screen.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.model.MediaSearchResult
import com.ethanjohnson.flipside.ui.components.FormatBadge

@Composable
fun DiscoverScreen(
    recommendations: List<MediaSearchResult>,
    onRecommendationClick: (
        MediaSearchResult
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Discover",
            style =
                MaterialTheme
                    .typography
                    .displaySmall
        )

        Text(
            text =
                "Explore physical releases based on your collection.",
            style =
                MaterialTheme
                    .typography
                    .bodyLarge,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        if (recommendations.isEmpty()) {
            Text(
                text =
                    "Add music to your collection to start discovering recommendations.",
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxWidth(),
                contentPadding =
                    PaddingValues(
                        vertical = 8.dp
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = recommendations,
                    key = {
                        "${it.source}:${it.externalId}"
                    }
                ) { recommendation ->

                    DiscoverRecommendationCard(
                        result =
                            recommendation,
                        onClick = {
                            onRecommendationClick(
                                recommendation
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverRecommendationCard(
    result: MediaSearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick
                )
    ) {
        Column(
            modifier =
                Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = result.title,
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                maxLines = 2,
                overflow =
                    TextOverflow.Ellipsis
            )

            Text(
                text =
                    buildString {
                        append(
                            result.subtitle
                        )

                        result.year?.let {
                            append(" • ")
                            append(it)
                        }
                    },
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            result.format?.let {
                FormatBadge(
                    text =
                        it.displayName
                )
            }
        }
    }
}