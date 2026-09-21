package com.ethanjohnson.flipside.data

import com.ethanjohnson.flipside.db.FlipSideDatabase
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock

class MediaRepository(
    private val database: FlipSideDatabase
) {

    private val _collectionItems =
        MutableStateFlow<List<MediaItem>>(emptyList())

    val collectionItems: StateFlow<List<MediaItem>> =
        _collectionItems.asStateFlow()

    private val _wishlistItems =
        MutableStateFlow<List<MediaItem>>(emptyList())

    val wishlistItems: StateFlow<List<MediaItem>> =
        _wishlistItems.asStateFlow()

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
                dateAdded = Clock.System
                    .now()
                    .toEpochMilliseconds(),
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
                dateAdded = Clock.System
                    .now()
                    .toEpochMilliseconds(),
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
            format = item.format.databaseValue,
            release_year = item.year?.toLong(),
            edition = item.edition,
            condition = item.condition,
            notes = item.notes,
            purchase_price = item.purchasePrice,
            date_added = item.dateAdded,
            is_owned = if (item.isOwned) 1L else 0L,
            is_wishlisted = if (item.isWishlisted) 1L else 0L,
            added_order = nextOrder()
        )
    }

    private fun refresh() {
        _collectionItems.value =
            database.mediaItemQueries
                .selectCollection(::mapMediaItem)
                .executeAsList()

        _wishlistItems.value =
            database.mediaItemQueries
                .selectWishlist(::mapMediaItem)
                .executeAsList()
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

        seedItems
            .reversed()
            .forEach { item ->
                insert(item)
            }
    }

    private fun nextOrder(): Long {
        return database.mediaItemQueries
            .maxAddedOrder()
            .executeAsOne() + 1
    }

    private fun generateId(): String {
        return "user-media-${Clock.System.now().toEpochMilliseconds()}"
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
        date_added: Long?,
        is_owned: Long,
        is_wishlisted: Long,
        added_order: Long
    ): MediaItem {
        val mediaFormat =
            MediaFormat.fromDatabaseValue(format)
                ?: error(
                    "Unknown media format stored in database: $format"
                )

        return MediaItem(
            id = id,
            title = title,
            subtitle = subtitle,
            format = mediaFormat,
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