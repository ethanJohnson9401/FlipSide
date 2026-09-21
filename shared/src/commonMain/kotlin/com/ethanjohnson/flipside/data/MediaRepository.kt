package com.ethanjohnson.flipside.data

import androidx.compose.runtime.mutableStateListOf
import com.ethanjohnson.flipside.db.FlipSideDatabase
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem

class MediaRepository(
    private val database: FlipSideDatabase
) {

    val collectionItems = mutableStateListOf<MediaItem>()
    val wishlistItems = mutableStateListOf<MediaItem>()

    init {
        seedDatabaseIfEmpty()
        refresh()
    }

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
        insert(
            item = MediaItem(
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
        )

        refresh()
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
        insert(
            item = MediaItem(
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
        )

        refresh()
    }

    private fun insert(
        item: MediaItem
    ) {
        database.mediaItemQueries.insertItem(
            id = item.id,
            title = item.title,
            subtitle = item.subtitle,
            format = item.format.name,
            release_year = item.year?.toLong(),
            edition = item.edition,
            condition = item.condition,
            notes = item.notes,
            purchase_price = item.purchasePrice,
            date_added = item.dateAdded,
            is_owned = if (item.isOwned) 1 else 0,
            is_wishlisted = if (item.isWishlisted) 1 else 0,
            added_order = nextOrder()
        )
    }

    private fun refresh() {
        val collection = database.mediaItemQueries
            .selectCollection(::mapMediaItem)
            .executeAsList()

        val wishlist = database.mediaItemQueries
            .selectWishlist(::mapMediaItem)
            .executeAsList()

        collectionItems.clear()
        collectionItems.addAll(collection)

        wishlistItems.clear()
        wishlistItems.addAll(wishlist)
    }

    private fun seedDatabaseIfEmpty() {
        val count = database.mediaItemQueries
            .countItems()
            .executeAsOne()

        if (count != 0L) {
            return
        }

        val seedItems =
            FakeMediaData.collectionItems +
                    FakeMediaData.wishlistItems

        seedItems.reversed().forEach { item ->
            insert(item)
        }
    }

    private fun nextOrder(): Long {
        return database.mediaItemQueries
            .maxAddedOrder()
            .executeAsOne() + 1
    }

    private fun generateId(): String {
        return "user-media-${nextOrder()}"
    }

    private fun mapMediaItem(
        id: String,
        title: String,
        subtitle: String,
        format: String,
        release_year: Long?,
        edition: String?,
        condition: String?,
        notes: String?,
        purchase_price: Double?,
        date_added: String?,
        is_owned: Long,
        is_wishlisted: Long,
        added_order: Long
    ): MediaItem {
        return MediaItem(
            id = id,
            title = title,
            subtitle = subtitle,
            format = MediaFormat.valueOf(format),
            year = release_year?.toInt(),
            edition = edition,
            condition = condition,
            notes = notes,
            purchasePrice = purchase_price,
            dateAdded = date_added,
            isOwned = is_owned != 0L,
            isWishlisted = is_wishlisted != 0L
        )
    }
}

private fun String?.nullIfBlank(): String? {
    return if (isNullOrBlank()) {
        null
    } else {
        trim()
    }
}