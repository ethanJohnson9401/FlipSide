package com.ethanjohnson.flipside.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.ui.components.FormatBadge

@Composable
fun MediaDetailScreen(
    item: MediaItem,
    onBack: () -> Unit = {}
) {
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
                    .widthIn(max = 1100.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedButton(
                    onClick = onBack
                ) {
                    Text("Back")
                }

                if (isWideLayout) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        MediaArtwork(
                            item = item,
                            modifier = Modifier.width(320.dp)
                        )

                        MediaDetails(
                            item = item,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        MediaArtwork(
                            item = item,
                            modifier = Modifier.fillMaxWidth()
                        )

                        MediaDetails(
                            item = item,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

@Composable
private fun MediaArtwork(
    item: MediaItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title.take(1).uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun MediaDetails(
    item: MediaItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = buildString {
                    append(item.subtitle)

                    item.year?.let { year ->
                        append(" • ")
                        append(year)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FormatBadge(
                text = item.format.displayName
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {}
            ) {
                Text(
                    if (item.isOwned) {
                        "Owned"
                    } else {
                        "Add to Collection"
                    }
                )
            }

            OutlinedButton(
                onClick = {}
            ) {
                Text(
                    if (item.isWishlisted) {
                        "Wishlisted"
                    } else {
                        "Add to Wishlist"
                    }
                )
            }
        }

        item.edition?.let {
            DetailSection(
                title = "Edition",
                value = it
            )
        }

        item.condition?.let {
            DetailSection(
                title = "Condition",
                value = it
            )
        }

        item.purchasePrice?.let {
            DetailSection(
                title = "Purchase Price",
                value = "$${formatPrice(it)}"
            )
        }

        item.dateAdded?.let {
            DetailSection(
                title = "Date Added",
                value = it
            )
        }

        item.notes?.let {
            DetailSection(
                title = "Notes",
                value = it
            )
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatPrice(
    price: Double
): String {
    val cents = (price * 100).toInt()

    val dollars = cents / 100
    val remainder = cents % 100

    return "$dollars.${remainder.toString().padStart(2, '0')}"
}