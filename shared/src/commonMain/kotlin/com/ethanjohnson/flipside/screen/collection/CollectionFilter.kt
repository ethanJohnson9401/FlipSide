package com.ethanjohnson.flipside.screen.collection

import com.ethanjohnson.flipside.model.MediaFormat

sealed class CollectionFilter(
    val label: String
) {
    data object All : CollectionFilter("All")

    data class Format(
        val format: MediaFormat
    ) : CollectionFilter(format.displayName)
}