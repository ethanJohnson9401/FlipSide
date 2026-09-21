package com.ethanjohnson.flipside.screen.detail

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.ui.components.FormatBadge
import kotlin.time.Clock
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun MediaDetailScreen(
    item: MediaItem,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onOwnedChange: (Boolean) -> Unit = {},
    onWishlistChange: (Boolean) -> Unit = {},
    onMoveToCollection: () -> Unit = {}
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showMoveToWishlistDialog by remember {
        mutableStateOf(false)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideLayout = maxWidth >= 900.dp

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
                    modifier = Modifier.height(16.dp)
                )

                DetailBackButton(
                    onBack = onBack
                )

                if (isWideLayout) {
                    DesktopDetailLayout(
                        item = item,
                        onEdit = onEdit,
                        onDeleteRequest = {
                            showDeleteDialog = true
                        },
                        onMoveToWishlistRequest = {
                            showMoveToWishlistDialog = true
                        },
                        onWishlistChange =
                            onWishlistChange,
                        onMoveToCollection =
                            onMoveToCollection
                    )
                } else {
                    MobileDetailLayout(
                        item = item,
                        onEdit = onEdit,
                        onDeleteRequest = {
                            showDeleteDialog = true
                        },
                        onMoveToWishlistRequest = {
                            showMoveToWishlistDialog = true
                        },
                        onWishlistChange =
                            onWishlistChange,
                        onMoveToCollection =
                            onMoveToCollection
                    )
                }

                Spacer(
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = "Delete ${item.title}?"
                )
            },
            text = {
                Text(
                    text =
                        "This will permanently remove this item from FlipSide. " +
                                "This action cannot be undone."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMoveToWishlistDialog) {
        AlertDialog(
            onDismissRequest = {
                showMoveToWishlistDialog = false
            },
            title = {
                Text("Move to Wishlist?")
            },
            text = {
                Text(
                    text =
                        "This will remove ${item.title} from your collection " +
                                "and keep it in your wishlist."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showMoveToWishlistDialog = false

                        onWishlistChange(true)
                        onOwnedChange(false)
                    }
                ) {
                    Text("Move")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showMoveToWishlistDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DesktopDetailLayout(
    item: MediaItem,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
    onMoveToWishlistRequest: () -> Unit,
    onWishlistChange: (Boolean) -> Unit,
    onMoveToCollection: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.Top
    ) {
        MediaArtwork(
            item = item,
            modifier = Modifier.width(280.dp)
        )

        MediaDetails(
            item = item,
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 680.dp),
            onEdit = onEdit,
            onDeleteRequest =
                onDeleteRequest,
            onMoveToWishlistRequest =
                onMoveToWishlistRequest,
            onWishlistChange =
                onWishlistChange,
            onMoveToCollection =
                onMoveToCollection
        )
    }
}

@Composable
private fun MobileDetailLayout(
    item: MediaItem,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
    onMoveToWishlistRequest: () -> Unit,
    onWishlistChange: (Boolean) -> Unit,
    onMoveToCollection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(22.dp)
    ) {
        MediaArtwork(
            item = item,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .align(
                    Alignment.CenterHorizontally
                )
        )

        MediaDetails(
            item = item,
            modifier = Modifier.fillMaxWidth(),
            onEdit = onEdit,
            onDeleteRequest =
                onDeleteRequest,
            onMoveToWishlistRequest =
                onMoveToWishlistRequest,
            onWishlistChange =
                onWishlistChange,
            onMoveToCollection =
                onMoveToCollection
        )
    }
}

@Composable
private fun DetailBackButton(
    onBack: () -> Unit
) {
    OutlinedButton(
        onClick = onBack
    ) {
        Text("Back")
    }
}

@Composable
private fun MediaArtwork(
    item: MediaItem,
    modifier: Modifier = Modifier
) {
    var imageFailed by remember(
        item.coverArtUrl
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
                item.coverArtUrl != null &&
                !imageFailed
            ) {
                AsyncImage(
                    model =
                        item.coverArtUrl,
                    contentDescription =
                        "Cover art for ${item.title}",
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
                        item.title
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
private fun MediaDetails(
    item: MediaItem,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
    onMoveToWishlistRequest: () -> Unit,
    onWishlistChange: (Boolean) -> Unit,
    onMoveToCollection: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(22.dp)
    ) {
        MediaIdentity(
            item = item
        )

        MediaActions(
            item = item,
            onWishlistChange =
                onWishlistChange,
            onMoveToCollection =
                onMoveToCollection
        )

        CollectorSummary(
            item = item
        )

        item.notes?.let { notes ->
            NotesSection(
                notes = notes
            )
        }

        ManageItemSection(
            item = item,
            onEdit = onEdit,
            onMoveToWishlistRequest =
                onMoveToWishlistRequest,
            onDeleteRequest =
                onDeleteRequest
        )
    }
}

@Composable
private fun MediaIdentity(
    item: MediaItem
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = item.title,
            style =
                MaterialTheme.typography.displaySmall,
            color =
                MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = mediaSubtitle(item),
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            FormatBadge(
                text = item.format.displayName
            )

            Surface(
                shape =
                    MaterialTheme.shapes.small,
                color =
                    MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = "Physical Copy",
                    modifier =
                        Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                    style =
                        MaterialTheme.typography.labelSmall,
                    color =
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun MediaActions(
    item: MediaItem,
    onWishlistChange: (Boolean) -> Unit,
    onMoveToCollection: () -> Unit
) {
    FlowRow(
        horizontalArrangement =
            Arrangement.spacedBy(10.dp),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        when {
            item.isOwned -> {
                Surface(
                    shape =
                        MaterialTheme.shapes.medium,
                    color =
                        MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Owned",
                        modifier =
                            Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 11.dp
                            ),
                        style =
                            MaterialTheme.typography.labelLarge,
                        color =
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            item.isWishlisted -> {
                Button(
                    onClick =
                        onMoveToCollection
                ) {
                    Text(
                        "Move to Collection"
                    )
                }
            }

            else -> {
                Button(
                    onClick =
                        onMoveToCollection
                ) {
                    Text(
                        "Add to Collection"
                    )
                }
            }
        }

        if (item.isOwned) {
            OutlinedButton(
                onClick = {
                    onWishlistChange(
                        !item.isWishlisted
                    )
                }
            ) {
                Text(
                    if (item.isWishlisted) {
                        "Remove from Wishlist"
                    } else {
                        "Add to Wishlist"
                    }
                )
            }
        } else if (item.isWishlisted) {
            OutlinedButton(
                onClick = {
                    onWishlistChange(false)
                }
            ) {
                Text(
                    "Remove from Wishlist"
                )
            }
        }
    }
}

@Composable
private fun CollectorSummary(
    item: MediaItem
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Copy Details",
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        FlowRow(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            item.edition?.let { edition ->
                MetadataCard(
                    label = "Edition",
                    value = edition,
                    modifier =
                        Modifier.widthIn(
                            min = 220.dp,
                            max = 320.dp
                        )
                )
            }

            item.condition?.let { condition ->
                MetadataCard(
                    label = "Condition",
                    value = condition,
                    modifier =
                        Modifier.widthIn(
                            min = 180.dp,
                            max = 240.dp
                        )
                )
            }

            item.purchasePrice?.let { price ->
                MetadataCard(
                    label =
                        "Purchase Price",
                    value =
                        "$${formatPrice(price)}",
                    modifier =
                        Modifier.widthIn(
                            min = 180.dp,
                            max = 220.dp
                        )
                )
            }

            item.dateAdded?.let { date ->
                MetadataCard(
                    label = "Date Added",
                    value =
                        formatDateAdded(date),
                    modifier =
                        Modifier.widthIn(
                            min = 220.dp,
                            max = 280.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun MetadataCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape =
            MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
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
                    MaterialTheme.typography.labelLarge,
                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value,
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotesSection(
    notes: String
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Notes",
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                MaterialTheme.shapes.large,
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
        ) {
            Text(
                text = notes,
                modifier =
                    Modifier.padding(18.dp),
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ManageItemSection(
    item: MediaItem,
    onEdit: () -> Unit,
    onMoveToWishlistRequest: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Manage Item",
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        FlowRow(
            horizontalArrangement =
                Arrangement.spacedBy(10.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onEdit
            ) {
                Text("Edit")
            }

            if (item.isOwned) {
                OutlinedButton(
                    onClick =
                        onMoveToWishlistRequest
                ) {
                    Text(
                        "Move to Wishlist"
                    )
                }
            }

            OutlinedButton(
                onClick =
                    onDeleteRequest
            ) {
                Text("Delete")
            }
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

private fun formatPrice(
    price: Double
): String {
    val cents =
        (price * 100).toInt()

    val dollars =
        cents / 100

    val remainder =
        cents % 100

    return "$dollars.${
        remainder
            .toString()
            .padStart(2, '0')
    }"
}

private fun formatDateAdded(
    timestamp: Long
): String {
    val now =
        Clock.System
            .now()
            .toEpochMilliseconds()

    val difference =
        (now - timestamp)
            .coerceAtLeast(0L)

    val minute = 60_000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        difference < minute -> {
            "Just now"
        }

        difference < hour -> {
            val minutes =
                difference / minute

            "$minutes min ago"
        }

        difference < day -> {
            val hours =
                difference / hour

            if (hours == 1L) {
                "1 hour ago"
            } else {
                "$hours hours ago"
            }
        }

        difference < 2 * day -> {
            "Yesterday"
        }

        else -> {
            val days =
                difference / day

            "$days days ago"
        }
    }
}