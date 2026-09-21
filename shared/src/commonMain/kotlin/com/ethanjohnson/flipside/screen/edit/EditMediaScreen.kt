package com.ethanjohnson.flipside.screen.edit

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem

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
    POOR("Poor");

    companion object {
        fun fromLabel(
            value: String?
        ): MediaCondition? {
            return entries.firstOrNull {
                it.label == value
            }
        }
    }
}

@Composable
fun EditMediaScreen(
    item: MediaItem,
    onCancel: () -> Unit,
    onSave: (MediaItem) -> Unit
) {
    var selectedFormat by remember(item.id) {
        mutableStateOf(item.format)
    }

    var title by remember(item.id) {
        mutableStateOf(item.title)
    }

    var subtitle by remember(item.id) {
        mutableStateOf(item.subtitle)
    }

    var year by remember(item.id) {
        mutableStateOf(
            item.year?.toString().orEmpty()
        )
    }

    var edition by remember(item.id) {
        mutableStateOf(
            item.edition.orEmpty()
        )
    }

    var condition by remember(item.id) {
        mutableStateOf(
            MediaCondition.fromLabel(
                item.condition
            )
        )
    }

    var conditionMenuExpanded by remember {
        mutableStateOf(false)
    }

    var purchasePrice by remember(item.id) {
        mutableStateOf(
            item.purchasePrice?.let {
                formatEditablePrice(it)
            }.orEmpty()
        )
    }

    var notes by remember(item.id) {
        mutableStateOf(
            item.notes.orEmpty()
        )
    }

    val canSave = title.isNotBlank()

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
                    .widthIn(max = 900.dp)
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
                    modifier = Modifier.height(20.dp)
                )

                EditHeader(
                    onCancel = onCancel
                )

                FormatSection(
                    selectedFormat = selectedFormat,
                    onFormatSelected = {
                        selectedFormat = it
                    }
                )

                BasicInfoSection(
                    title = title,
                    onTitleChange = {
                        title = it
                    },
                    subtitle = subtitle,
                    onSubtitleChange = {
                        subtitle = it
                    },
                    year = year,
                    onYearChange = {
                        year = it
                            .filter(Char::isDigit)
                            .take(4)
                    }
                )

                CopyDetailsSection(
                    edition = edition,
                    onEditionChange = {
                        edition = it
                    },
                    condition = condition,
                    conditionMenuExpanded =
                        conditionMenuExpanded,
                    onConditionMenuExpandedChange = {
                        conditionMenuExpanded = it
                    },
                    onConditionChange = {
                        condition = it
                    },
                    purchasePrice = purchasePrice,
                    onPurchasePriceChange = {
                        purchasePrice =
                            sanitizePrice(it)
                    },
                    showPurchasePrice = item.isOwned
                )

                NotesSection(
                    notes = notes,
                    onNotesChange = {
                        notes = it
                    }
                )

                SaveSection(
                    canSave = canSave,
                    onCancel = onCancel,
                    onSave = {
                        onSave(
                            item.copy(
                                title = title.trim(),
                                subtitle = subtitle.trim(),
                                format = selectedFormat,
                                year = year.toIntOrNull(),
                                edition = edition
                                    .trim()
                                    .ifBlank { null },
                                condition =
                                    condition?.label,
                                purchasePrice =
                                    if (item.isOwned) {
                                        purchasePrice
                                            .toDoubleOrNull()
                                    } else {
                                        null
                                    },
                                notes = notes
                                    .trim()
                                    .ifBlank { null }
                            )
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun EditHeader(
    onCancel: () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onCancel
        ) {
            Text("Back")
        }

        Text(
            text = "Edit Media",
            style =
                MaterialTheme.typography.displaySmall,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Update the details for this physical copy.",
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FormatSection(
    selectedFormat: MediaFormat,
    onFormatSelected: (MediaFormat) -> Unit
) {
    FormSection(
        title = "Format"
    ) {
        FlowRow(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            MediaFormat.entries.forEach { format ->
                FilterChip(
                    selected =
                        selectedFormat == format,
                    onClick = {
                        onFormatSelected(format)
                    },
                    label = {
                        Text(format.displayName)
                    }
                )
            }
        }
    }
}

@Composable
private fun BasicInfoSection(
    title: String,
    onTitleChange: (String) -> Unit,
    subtitle: String,
    onSubtitleChange: (String) -> Unit,
    year: String,
    onYearChange: (String) -> Unit
) {
    FormSection(
        title = "Media Information"
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Title")
                },
                supportingText = {
                    Text("Required")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = onSubtitleChange,
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
                onValueChange = onYearChange,
                modifier =
                    Modifier.widthIn(max = 220.dp),
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
        }
    }
}

@Composable
private fun CopyDetailsSection(
    edition: String,
    onEditionChange: (String) -> Unit,
    condition: MediaCondition?,
    conditionMenuExpanded: Boolean,
    onConditionMenuExpandedChange: (Boolean) -> Unit,
    onConditionChange: (MediaCondition) -> Unit,
    purchasePrice: String,
    onPurchasePriceChange: (String) -> Unit,
    showPurchasePrice: Boolean
) {
    FormSection(
        title = "Copy Details"
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = edition,
                onValueChange = onEditionChange,
                modifier =
                    Modifier.fillMaxWidth(),
                label = {
                    Text("Edition")
                },
                singleLine = true
            )

            Box {
                OutlinedButton(
                    onClick = {
                        onConditionMenuExpandedChange(
                            true
                        )
                    }
                ) {
                    Text(
                        condition?.label
                            ?: "Select condition"
                    )
                }

                DropdownMenu(
                    expanded =
                        conditionMenuExpanded,
                    onDismissRequest = {
                        onConditionMenuExpandedChange(
                            false
                        )
                    }
                ) {
                    MediaCondition.entries
                        .forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.label
                                    )
                                },
                                onClick = {
                                    onConditionChange(
                                        option
                                    )

                                    onConditionMenuExpandedChange(
                                        false
                                    )
                                }
                            )
                        }
                }
            }

            if (showPurchasePrice) {
                OutlinedTextField(
                    value = purchasePrice,
                    onValueChange =
                        onPurchasePriceChange,
                    modifier =
                        Modifier.widthIn(
                            max = 260.dp
                        ),
                    label = {
                        Text("Purchase price")
                    },
                    prefix = {
                        Text("$")
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
private fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    FormSection(
        title = "Notes"
    ) {
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            label = {
                Text("Notes about this copy")
            }
        )
    }
}

@Composable
private fun SaveSection(
    canSave: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Save changes?",
                    style =
                        MaterialTheme.typography.titleMedium,
                    color =
                        MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Your changes will be saved locally.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onSave,
                    enabled = canSave
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.onBackground
        )

        content()
    }
}

private fun sanitizePrice(
    value: String
): String {
    val filtered = value.filter {
        it.isDigit() || it == '.'
    }

    val firstDecimal =
        filtered.indexOf('.')

    if (firstDecimal == -1) {
        return filtered
    }

    val whole =
        filtered.substring(
            0,
            firstDecimal
        )

    val decimal =
        filtered
            .substring(firstDecimal + 1)
            .filter(Char::isDigit)
            .take(2)

    return "$whole.$decimal"
}

private fun formatEditablePrice(
    price: Double
): String {
    val cents =
        (price * 100).toLong()

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