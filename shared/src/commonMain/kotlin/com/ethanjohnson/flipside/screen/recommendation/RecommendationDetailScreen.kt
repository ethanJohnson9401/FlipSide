package com.ethanjohnson.flipside.screen.recommendation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.ethanjohnson.flipside.model.MediaSearchResult
import com.ethanjohnson.flipside.ui.components.FormatBadge

@Composable
fun RecommendationDetailScreen(
    result: MediaSearchResult,
    onBack: () -> Unit,
    onAddToCollection: () -> Unit,
    onAddToWishlist: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 1100.dp)
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = horizontalPadding
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(24.dp)
            ) {
                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                OutlinedButton(
                    onClick = onBack
                ) {
                    Text("Back")
                }

                if (isWideLayout) {
                    DesktopRecommendationLayout(
                        result = result,
                        onAddToCollection =
                            onAddToCollection,
                        onAddToWishlist =
                            onAddToWishlist
                    )
                } else {
                    MobileRecommendationLayout(
                        result = result,
                        onAddToCollection =
                            onAddToCollection,
                        onAddToWishlist =
                            onAddToWishlist
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun DesktopRecommendationLayout(
    result: MediaSearchResult,
    onAddToCollection: () -> Unit,
    onAddToWishlist: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(32.dp),
        verticalAlignment =
            Alignment.Top
    ) {
        RecommendationArtwork(
            result = result,
            modifier =
                Modifier.width(280.dp)
        )

        RecommendationDetails(
            result = result,
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 680.dp),
            onAddToCollection =
                onAddToCollection,
            onAddToWishlist =
                onAddToWishlist
        )
    }
}

@Composable
private fun MobileRecommendationLayout(
    result: MediaSearchResult,
    onAddToCollection: () -> Unit,
    onAddToWishlist: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(22.dp)
    ) {
        RecommendationArtwork(
            result = result,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .align(
                    Alignment.CenterHorizontally
                )
        )

        RecommendationDetails(
            result = result,
            modifier =
                Modifier.fillMaxWidth(),
            onAddToCollection =
                onAddToCollection,
            onAddToWishlist =
                onAddToWishlist
        )
    }
}

@Composable
private fun RecommendationArtwork(
    result: MediaSearchResult,
    modifier: Modifier = Modifier
) {
    var imageFailed by remember(
        result.coverArtUrl
    ) {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier,
        shape =
            MaterialTheme.shapes.extraLarge,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
            )
    ) {
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
                result.coverArtUrl != null &&
                !imageFailed
            ) {
                AsyncImage(
                    model =
                        result.coverArtUrl,
                    contentDescription =
                        "Cover art for ${result.title}",
                    modifier =
                        Modifier.fillMaxSize(),
                    contentScale =
                        ContentScale.Crop,
                    onError = {
                        imageFailed = true
                    }
                )
            } else {
                Text(
                    text =
                        result.title
                            .take(1)
                            .uppercase(),
                    style =
                        MaterialTheme
                            .typography
                            .displayLarge,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun RecommendationDetails(
    result: MediaSearchResult,
    modifier: Modifier = Modifier,
    onAddToCollection: () -> Unit,
    onAddToWishlist: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(22.dp)
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = result.title,
                style =
                    MaterialTheme
                        .typography
                        .displaySmall,
                color =
                    MaterialTheme
                        .colorScheme
                        .onBackground,
                maxLines = 3,
                overflow =
                    TextOverflow.Ellipsis
            )

            Text(
                text =
                    recommendationSubtitle(
                        result
                    ),
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            FlowRow(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                result.format?.let {
                    FormatBadge(
                        text =
                            it.displayName
                    )
                }

                Surface(
                    shape =
                        MaterialTheme.shapes.small,
                    color =
                        MaterialTheme
                            .colorScheme
                            .secondaryContainer
                ) {
                    Text(
                        text =
                            result.source,
                        modifier =
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .labelSmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSecondaryContainer
                    )
                }
            }
        }

        RecommendationActions(
            result = result,
            onAddToCollection =
                onAddToCollection,
            onAddToWishlist =
                onAddToWishlist
        )

        RecommendationMetadata(
            result = result
        )
    }
}

@Composable
private fun RecommendationActions(
    result: MediaSearchResult,
    onAddToCollection: () -> Unit,
    onAddToWishlist: () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        FlowRow(
            horizontalArrangement =
                Arrangement.spacedBy(10.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick =
                    onAddToCollection,
                enabled =
                    result.format != null
            ) {
                Text(
                    "Add to Collection"
                )
            }

            OutlinedButton(
                onClick =
                    onAddToWishlist,
                enabled =
                    result.format != null
            ) {
                Text(
                    "Add to Wishlist"
                )
            }
        }

        if (result.format == null) {
            Text(
                text =
                    "FlipSide could not determine the physical format for this release.",
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecommendationMetadata(
    result: MediaSearchResult
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Release Details",
            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        FlowRow(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            result.year?.let { year ->
                MetadataCard(
                    label = "Year",
                    value =
                        year.toString()
                )
            }

            result.format?.let { format ->
                MetadataCard(
                    label = "Format",
                    value =
                        format.displayName
                )
            }

            result.edition?.let { edition ->
                MetadataCard(
                    label = "Edition",
                    value =
                        edition
                )
            }

            MetadataCard(
                label = "Source",
                value =
                    result.source
            )
        }
    }
}

@Composable
private fun MetadataCard(
    label: String,
    value: String
) {
    Card(
        modifier =
            Modifier.widthIn(
                min = 180.dp,
                max = 300.dp
            ),
        shape =
            MaterialTheme.shapes.large
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = label,
                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )

            Text(
                text = value,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

private fun recommendationSubtitle(
    result: MediaSearchResult
): String {
    return buildString {
        append(result.subtitle)

        result.year?.let { year ->
            append(" • ")
            append(year)
        }
    }
}