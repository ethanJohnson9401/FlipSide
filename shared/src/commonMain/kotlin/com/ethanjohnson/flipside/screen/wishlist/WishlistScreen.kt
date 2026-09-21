package com.ethanjohnson.flipside.screen.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.data.FakeMediaData
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.screen.collection.CollectionFilter
import com.ethanjohnson.flipside.ui.components.MediaCard

@Composable
fun WishlistScreen(
    onMediaClick: (MediaItem) -> Unit = {}
) {
    val wishlistItems = FakeMediaData.wishlistItems

    var searchText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var selectedFilter by remember {
        mutableStateOf<CollectionFilter>(CollectionFilter.All)
    }

    val filters = remember {
        listOf(
            CollectionFilter.All,
            CollectionFilter.Format(MediaFormat.VINYL),
            CollectionFilter.Format(MediaFormat.CD),
            CollectionFilter.Format(MediaFormat.CASSETTE),
            CollectionFilter.Format(MediaFormat.VHS),
            CollectionFilter.Format(MediaFormat.DVD),
            CollectionFilter.Format(MediaFormat.BLURAY),
            CollectionFilter.Format(MediaFormat.GAME),
            CollectionFilter.Format(MediaFormat.BOOK)
        )
    }

    val filteredItems = wishlistItems.filter { item ->
        val matchesSearch =
            searchText.text.isBlank() ||
                    item.title.contains(
                        searchText.text,
                        ignoreCase = true
                    ) ||
                    item.subtitle.contains(
                        searchText.text,
                        ignoreCase = true
                    )

        val matchesFilter =
            when (val filter = selectedFilter) {
                CollectionFilter.All -> true

                is CollectionFilter.Format ->
                    item.format == filter.format
            }

        matchesSearch && matchesFilter
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideLayout = maxWidth >= 900.dp

        val horizontalPadding = if (isWideLayout) {
            32.dp
        } else {
            16.dp
        }

        val minCardWidth = if (isWideLayout) {
            190.dp
        } else {
            150.dp
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

                Text(
                    text = "Wishlist",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${wishlistItems.size} items you’re looking for",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Search your wishlist...")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = {
                                selectedFilter = filter
                            },
                            label = {
                                Text(filter.label)
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "${filteredItems.size} results",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (filteredItems.isEmpty()) {
                    EmptyWishlistState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(
                            minSize = minCardWidth
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        gridItems(
                            items = filteredItems,
                            key = { item ->
                                item.id
                            }
                        ) { item ->
                            MediaCard(
                                title = item.title,
                                subtitle = mediaSubtitle(item),
                                format = item.format.displayName,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onMediaClick(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyWishlistState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Nothing here",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Try another search or format filter.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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