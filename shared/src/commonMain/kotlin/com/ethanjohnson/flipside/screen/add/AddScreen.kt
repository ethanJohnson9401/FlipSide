package com.ethanjohnson.flipside.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ethanjohnson.flipside.data.search.MediaSearchRepository
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaSearchResult
import com.ethanjohnson.flipside.ui.components.FormatBadge
import kotlinx.coroutines.launch

private enum class AddMode {
    MANUAL,
    SEARCH
}

private enum class AddDestination {
    COLLECTION,
    WISHLIST
}

private enum class MediaCondition(
    val label: String
) {
    MINT("Mint"),
    NEAR_MINT("Near Mint"),
    VERY_GOOD_PLUS("Very Good+"),
    VERY_GOOD("Very Good"),
    GOOD_PLUS("Good+"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor")
}

@Composable
fun AddScreen(
    searchRepository: MediaSearchRepository,
    onAddToCollection: (
        String,
        String,
        MediaFormat,
        Int?,
        String?,
        String?,
        Double?,
        String?,
        String?
    ) -> Unit,
    onAddToWishlist: (
        String,
        String,
        MediaFormat,
        Int?,
        String?,
        String?,
        String?,
        String?
    ) -> Unit
) {
    var addMode by remember {
        mutableStateOf(AddMode.MANUAL)
    }

    var destination by remember {
        mutableStateOf(AddDestination.COLLECTION)
    }

    var selectedFormat by remember {
        mutableStateOf<MediaFormat?>(MediaFormat.VINYL)
    }

    var title by remember {
        mutableStateOf("")
    }

    var subtitle by remember {
        mutableStateOf("")
    }

    var year by remember {
        mutableStateOf("")
    }

    var edition by remember {
        mutableStateOf("")
    }

    var condition by remember {
        mutableStateOf<MediaCondition?>(null)
    }

    var conditionExpanded by remember {
        mutableStateOf(false)
    }

    var purchasePrice by remember {
        mutableStateOf("")
    }

    var notes by remember {
        mutableStateOf("")
    }

    var coverArtUrl by remember {
        mutableStateOf<String?>(null)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var searchResults by remember {
        mutableStateOf<List<MediaSearchResult>>(
            emptyList()
        )
    }

    var isSearching by remember {
        mutableStateOf(false)
    }

    var searchError by remember {
        mutableStateOf<String?>(null)
    }

    val coroutineScope =
        rememberCoroutineScope()

    fun clearForm() {
        selectedFormat =
            MediaFormat.VINYL

        title = ""
        subtitle = ""
        year = ""
        edition = ""
        condition = null
        purchasePrice = ""
        notes = ""
        coverArtUrl = null

        searchQuery = ""
        searchResults = emptyList()
        searchError = null
    }

    fun populateFromSearchResult(
        result: MediaSearchResult
    ) {
        title =
            result.title

        subtitle =
            result.subtitle

        year =
            result.year
                ?.toString()
                .orEmpty()

        edition =
            result.edition
                .orEmpty()

        coverArtUrl =
            result.coverArtUrl

        selectedFormat =
            result.format

        addMode =
            AddMode.MANUAL
    }

    BoxWithConstraints(
        modifier =
            Modifier.fillMaxSize()
    ) {
        val horizontalPadding =
            if (maxWidth >= 900.dp) {
                32.dp
            } else {
                16.dp
            }

        Box(
            modifier =
                Modifier.fillMaxSize(),
            contentAlignment =
                Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 900.dp)
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal =
                            horizontalPadding
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(24.dp)
            ) {
                Spacer(
                    Modifier.height(20.dp)
                )

                Text(
                    text = "Add Media",
                    style =
                        MaterialTheme
                            .typography
                            .displaySmall
                )

                Text(
                    text =
                        "Add a physical copy to your collection or wishlist.",
                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected =
                            addMode ==
                                    AddMode.MANUAL,
                        onClick = {
                            addMode =
                                AddMode.MANUAL
                        },
                        label = {
                            Text("Manual")
                        }
                    )

                    FilterChip(
                        selected =
                            addMode ==
                                    AddMode.SEARCH,
                        onClick = {
                            addMode =
                                AddMode.SEARCH
                        },
                        label = {
                            Text("Search")
                        }
                    )
                }

                if (
                    addMode ==
                    AddMode.SEARCH
                ) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value =
                                searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            modifier =
                                Modifier.weight(1f),
                            label = {
                                Text(
                                    "Title, artist..."
                                )
                            },
                            singleLine = true
                        )

                        Button(
                            enabled =
                                searchQuery.isNotBlank() &&
                                        !isSearching,
                            onClick = {
                                coroutineScope.launch {
                                    isSearching =
                                        true

                                    searchError = null

                                    try {
                                        searchResults =
                                            searchRepository
                                                .search(
                                                    searchQuery
                                                )
                                    } catch (exception: Exception) {
                                        searchResults =
                                            emptyList()

                                        searchError =
                                            exception.message
                                                ?.takeIf {
                                                    it.isNotBlank()
                                                }
                                                ?: "Search failed. Please try again."
                                    } finally {
                                        isSearching =
                                            false
                                    }
                                }
                            }
                        ) {
                            Text(
                                if (isSearching) {
                                    "Searching..."
                                } else {
                                    "Search"
                                }
                            )
                        }
                    }

                    searchError?.let { error ->
                        Text(
                            text = error,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }

                    searchResults.forEach {
                            result ->

                        SearchResultCard(
                            result =
                                result,
                            onClick = {
                                populateFromSearchResult(
                                    result
                                )
                            }
                        )
                    }
                }

                if (
                    addMode ==
                    AddMode.MANUAL
                ) {
                    Text(
                        text = "Add To",
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected =
                                destination ==
                                        AddDestination.COLLECTION,
                            onClick = {
                                destination =
                                    AddDestination.COLLECTION
                            },
                            label = {
                                Text("Collection")
                            }
                        )

                        FilterChip(
                            selected =
                                destination ==
                                        AddDestination.WISHLIST,
                            onClick = {
                                destination =
                                    AddDestination.WISHLIST
                            },
                            label = {
                                Text("Wishlist")
                            }
                        )
                    }

                    Text(
                        text = "Format",
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )

                    FlowRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        MediaFormat.entries
                            .forEach { format ->

                                FilterChip(
                                    selected =
                                        selectedFormat ==
                                                format,
                                    onClick = {
                                        selectedFormat =
                                            format
                                    },
                                    label = {
                                        Text(
                                            format.displayName
                                        )
                                    }
                                )
                            }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        label = {
                            Text("Title")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = {
                            subtitle = it
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Artist, director, developer, or author"
                            )
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = year,
                        onValueChange = {
                            year = it
                                .filter(
                                    Char::isDigit
                                )
                                .take(4)
                        },
                        label = {
                            Text("Year")
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Number
                            ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = edition,
                        onValueChange = {
                            edition = it
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        label = {
                            Text("Edition")
                        }
                    )

                    Box {
                        OutlinedButton(
                            onClick = {
                                conditionExpanded =
                                    true
                            }
                        ) {
                            Text(
                                condition
                                    ?.label
                                    ?: "Select condition"
                            )
                        }

                        DropdownMenu(
                            expanded =
                                conditionExpanded,
                            onDismissRequest = {
                                conditionExpanded =
                                    false
                            }
                        ) {
                            MediaCondition.entries
                                .forEach {
                                        option ->

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option.label
                                            )
                                        },
                                        onClick = {
                                            condition =
                                                option

                                            conditionExpanded =
                                                false
                                        }
                                    )
                                }
                        }
                    }

                    if (
                        destination ==
                        AddDestination.COLLECTION
                    ) {
                        OutlinedTextField(
                            value =
                                purchasePrice,
                            onValueChange = {
                                purchasePrice =
                                    sanitizePrice(it)
                            },
                            label = {
                                Text(
                                    "Purchase price"
                                )
                            },
                            prefix = {
                                Text("$")
                            },
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Decimal
                                )
                        )
                    }

                    OutlinedTextField(
                        value =
                            notes,
                        onValueChange = {
                            notes = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        label = {
                            Text("Notes")
                        }
                    )

                    Button(
                        enabled =
                            title.isNotBlank() &&
                                    selectedFormat != null,
                        onClick = {
                            val format =
                                selectedFormat
                                    ?: return@Button

                            if (
                                destination ==
                                AddDestination.COLLECTION
                            ) {
                                onAddToCollection(
                                    title.trim(),
                                    subtitle.trim(),
                                    format,
                                    year.toIntOrNull(),
                                    edition
                                        .trim()
                                        .ifBlank {
                                            null
                                        },
                                    condition?.label,
                                    purchasePrice
                                        .toDoubleOrNull(),
                                    notes
                                        .trim()
                                        .ifBlank {
                                            null
                                        },
                                    coverArtUrl
                                )
                            } else {
                                onAddToWishlist(
                                    title.trim(),
                                    subtitle.trim(),
                                    format,
                                    year.toIntOrNull(),
                                    edition
                                        .trim()
                                        .ifBlank {
                                            null
                                        },
                                    condition?.label,
                                    notes
                                        .trim()
                                        .ifBlank {
                                            null
                                        },
                                    coverArtUrl
                                )
                            }

                            clearForm()
                        }
                    ) {
                        Text(
                            if (
                                destination ==
                                AddDestination.COLLECTION
                            ) {
                                "Add to Collection"
                            } else {
                                "Add to Wishlist"
                            }
                        )
                    }
                }

                Spacer(
                    Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    result: MediaSearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement =
                Arrangement.spacedBy(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            SearchArtwork(result)

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text =
                        result.title,
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,
                    maxLines = 2,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Text(
                    text = buildString {
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

                result.edition?.let {
                    Text(
                        text = it,
                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }
            }

            result.format?.let {
                FormatBadge(
                    text =
                        it.displayName
                )
            }
        }
    }
}

@Composable
private fun SearchArtwork(
    result: MediaSearchResult
) {
    var failed by remember(
        result.coverArtUrl
    ) {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(
                RoundedCornerShape(
                    10.dp
                )
            )
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
            !failed
        ) {
            AsyncImage(
                model =
                    result.coverArtUrl,
                contentDescription =
                    result.title,
                modifier =
                    Modifier.fillMaxSize(),
                contentScale =
                    ContentScale.Crop,
                onError = {
                    failed = true
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
                        .headlineLarge
            )
        }
    }
}

private fun sanitizePrice(
    value: String
): String {
    val filtered =
        value.filter {
            it.isDigit() ||
                    it == '.'
        }

    val decimal =
        filtered.indexOf('.')

    if (decimal == -1) {
        return filtered
    }

    return filtered.substring(
        0,
        decimal + 1
    ) +
            filtered
                .substring(
                    decimal + 1
                )
                .filter(
                    Char::isDigit
                )
                .take(2)
}