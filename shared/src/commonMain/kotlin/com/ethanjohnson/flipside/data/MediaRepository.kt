package com.ethanjohnson.flipside.data

import androidx.compose.runtime.mutableStateListOf
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem

class MediaRepository {

    val collectionItems = mutableStateListOf<MediaItem>().apply {
        addAll(FakeMediaData.collectionItems)
    }

    val wishlistItems = mutableStateListOf<MediaItem>().apply {
        addAll(FakeMediaData.wishlistItems)
    }

    private var nextGeneratedId = 1

    fun addToCollection(
        title: String,
        subtitle: String,
        format: MediaFormat,
        year: Int?,
        edition: String?,
        condition: String?,
        purchasePrice: Double?,
        notes: String?
    ) {
        val item = MediaItem(
            id = generateId(),
            title = title.trim(),
            subtitle = subtitle.trim(),
            format = format,
            year = year,
            edition = edition.nullIfBlank(),
            condition = condition.nullIfBlank(),
            notes = notes.nullIfBlank(),
            purchasePrice = purchasePrice,
            dateAdded = "Just now",
            isOwned = true,
            isWishlisted = false
        )

        collectionItems.add(
            index = 0,
            element = item
        )
    }

    fun addToWishlist(
        title: String,
        subtitle: String,
        format: MediaFormat,
        year: Int?,
        edition: String?,
        condition: String?,
        notes: String?
    ) {
        val item = MediaItem(
            id = generateId(),
            title = title.trim(),
            subtitle = subtitle.trim(),
            format = format,
            year = year,
            edition = edition.nullIfBlank(),
            condition = condition.nullIfBlank(),
            notes = notes.nullIfBlank(),
            purchasePrice = null,
            dateAdded = "Just now",
            isOwned = false,
            isWishlisted = true
        )

        wishlistItems.add(
            index = 0,
            element = item
        )
    }

    private fun generateId(): String {
        return "user-media-${nextGeneratedId++}"
    }
}

private fun String?.nullIfBlank(): String? {
    return if (this.isNullOrBlank()) {
        null
    } else {
        trim()
    }
}