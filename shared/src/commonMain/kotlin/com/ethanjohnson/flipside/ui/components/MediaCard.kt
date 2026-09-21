package com.ethanjohnson.flipside.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun MediaCard(
    title: String,
    subtitle: String,
    format: String,
    coverArtUrl: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var imageFailed by remember(
        coverArtUrl
    ) {
        mutableStateOf(false)
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        shape =
            MaterialTheme.shapes.large,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
            )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                    ),
                contentAlignment =
                    Alignment.Center
            ) {
                if (
                    coverArtUrl != null &&
                    !imageFailed
                ) {
                    AsyncImage(
                        model =
                            coverArtUrl,
                        contentDescription =
                            "Cover art for $title",
                        modifier =
                            Modifier.fillMaxSize(),
                        contentScale =
                            ContentScale.Crop,
                        onError = {
                            imageFailed =
                                true
                        }
                    )
                } else {
                    Text(
                        text =
                            title
                                .take(1)
                                .uppercase(),
                        style =
                            MaterialTheme
                                .typography
                                .displayMedium,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimaryContainer
                    )
                }
            }

            Column(
                modifier =
                    Modifier.padding(14.dp)
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    maxLines = 2,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                FormatBadge(
                    text = format
                )
            }
        }
    }
}