package com.ethanjohnson.flipside.screen.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.data.FakeMediaData
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.ui.components.FormatBadge
import com.ethanjohnson.flipside.ui.components.MediaCard

private enum class CollectionSort(
    val label: String
) {
    RECENTLY_ADDED("Recently Added"),
    OLDEST_ADDED("Oldest Added"),
    TITLE_ASCENDING("Title A–Z"),
    TITLE_DESCENDING("Title Z–A"),
    YEAR_NEWEST("Year Newest"),
    YEAR_OLDEST("Year Oldest")
}

private enum class CollectionViewMode {
    GRID,
    LIST
}

@Composable
fun CollectionScreen(
    onMediaClick: (MediaItem) -> Unit = {}
) {
    val collectionItems = FakeMediaData.collectionItems

    var searchText by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var selectedFilter by remember {
        mutableStateOf<CollectionFilter>(CollectionFilter.All)
    }

    var selectedSort by remember {
        mutableStateOf(CollectionSort.RECENTLY_ADDED)
    }

    var sortMenuExpanded by remember {
        mutableStateOf(false)
    }

    var viewMode by remember {
        mutableStateOf(CollectionViewMode.GRID)
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

    val filteredItems = collectionItems.filter { item ->
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

    val sortedItems = when (selectedSort) {
        CollectionSort.RECENTLY_ADDED -> {
            filteredItems
        }

        CollectionSort.OLDEST_ADDED -> {
            filteredItems.reversed()
        }

        CollectionSort.TITLE_ASCENDING -> {
            filteredItems.sortedBy {
                it.title.lowercase()
            }
        }

        CollectionSort.TITLE_DESCENDING -> {
            filteredItems.sortedByDescending {
                it.title.lowercase()
            }
        }

        CollectionSort.YEAR_NEWEST -> {
            filteredItems.sortedByDescending {
                it.year ?: Int.MIN_VALUE
            }
        }

        CollectionSort.YEAR_OLDEST -> {
            filteredItems.sortedBy {
                it.year ?: Int.MAX_VALUE
            }
        }
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
                    text = "Collection",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${collectionItems.size} items in your archive",
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
                        Text("Search your collection...")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lazyItems(filters) { filter ->
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

                CollectionToolbar(
                    resultCount = sortedItems.size,
                    selectedSort = selectedSort,
                    sortMenuExpanded = sortMenuExpanded,
                    onSortMenuExpandedChange = {
                        sortMenuExpanded = it
                    },
                    onSortSelected = { sort ->
                        selectedSort = sort
                        sortMenuExpanded = false
                    },
                    viewMode = viewMode,
                    onViewModeChanged = {
                        viewMode = it
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (sortedItems.isEmpty()) {
                    EmptyCollectionState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    when (viewMode) {
                        CollectionViewMode.GRID -> {
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
                                    items = sortedItems,
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

                        CollectionViewMode.LIST -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                lazyItems(
                                    items = sortedItems,
                                    key = { item ->
                                        item.id
                                    }
                                ) { item ->
                                    CollectionListItem(
                                        item = item,
                                        onClick = {
                                            onMediaClick(item)
                                        }
                                    )
                                }

                                item {
                                    Spacer(
                                        modifier = Modifier.height(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionToolbar(
    resultCount: Int,
    selectedSort: CollectionSort,
    sortMenuExpanded: Boolean,
    onSortMenuExpandedChange: (Boolean) -> Unit,
    onSortSelected: (CollectionSort) -> Unit,
    viewMode: CollectionViewMode,
    onViewModeChanged: (CollectionViewMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$resultCount results",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                TextButton(
                    onClick = {
                        onSortMenuExpandedChange(true)
                    }
                ) {
                    Text(
                        text = selectedSort.label,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = {
                        onSortMenuExpandedChange(false)
                    }
                ) {
                    CollectionSort.entries.forEach { sort ->
                        DropdownMenuItem(
                            text = {
                                Text(sort.label)
                            },
                            onClick = {
                                onSortSelected(sort)
                            }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    onViewModeChanged(
                        if (viewMode == CollectionViewMode.GRID) {
                            CollectionViewMode.LIST
                        } else {
                            CollectionViewMode.GRID
                        }
                    )
                }
            ) {
                Text(
                    text = if (viewMode == CollectionViewMode.GRID) {
                        "List"
                    } else {
                        "Grid"
                    }
                )
            }
        }
    }
}

@Composable
private fun CollectionListItem(
    item: MediaItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = mediaSubtitle(item),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                FormatBadge(
                    text = item.format.displayName
                )
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

@Composable
private fun EmptyCollectionState(
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
                text = "Nothing found",
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