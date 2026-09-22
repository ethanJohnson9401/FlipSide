package com.ethanjohnson.flipside.data

import com.ethanjohnson.flipside.db.FlipSideDatabase
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.uuid.Uuid

class MediaRepository(
    val database: FlipSideDatabase
) {

    private val _collectionItems =
        MutableStateFlow<List<MediaItem>>(
            emptyList()
        )

    val collectionItems:
            StateFlow<List<MediaItem>> =
        _collectionItems.asStateFlow()

    private val _wishlistItems =
        MutableStateFlow<List<MediaItem>>(
            emptyList()
        )

    val wishlistItems:
            StateFlow<List<MediaItem>> =
        _wishlistItems.asStateFlow()

    init {
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
        notes: String?,
        coverArtUrl: String? = null
    ) {
        val now =
            Clock.System
                .now()
                .toEpochMilliseconds()

        val item =
            MediaItem(
                id = generateId(),
                title = title.trim(),
                subtitle = subtitle.trim(),
                format = format,
                year = year,
                edition =
                    edition.nullIfBlank(),
                condition =
                    condition.nullIfBlank(),
                notes =
                    notes.nullIfBlank(),
                purchasePrice =
                    purchasePrice,
                dateAdded =
                    now,
                coverArtUrl =
                    coverArtUrl,
                isOwned =
                    true,
                isWishlisted =
                    false
            )

        insertMediaItem(item)
        refresh()
    }

    fun addToWishlist(
        title: String,
        subtitle: String,
        format: MediaFormat,
        year: Int?,
        edition: String?,
        condition: String?,
        notes: String?,
        coverArtUrl: String? = null
    ) {
        val now =
            Clock.System
                .now()
                .toEpochMilliseconds()

        val item =
            MediaItem(
                id = generateId(),
                title = title.trim(),
                subtitle = subtitle.trim(),
                format = format,
                year = year,
                edition =
                    edition.nullIfBlank(),
                condition =
                    condition.nullIfBlank(),
                notes =
                    notes.nullIfBlank(),
                purchasePrice =
                    null,
                dateAdded =
                    now,
                coverArtUrl =
                    coverArtUrl,
                isOwned =
                    false,
                isWishlisted =
                    true
            )

        insertMediaItem(item)
        refresh()
    }

    fun updateMediaItem(
        item: MediaItem
    ) {
        database
            .mediaItemQueries
            .updateItem(
                title =
                    item.title.trim(),
                subtitle =
                    item.subtitle.trim(),
                format =
                    item.format.databaseValue,
                release_year =
                    item.year?.toLong(),
                edition =
                    item.edition.nullIfBlank(),
                condition =
                    item.condition.nullIfBlank(),
                notes =
                    item.notes.nullIfBlank(),
                purchase_price =
                    item.purchasePrice,
                cover_art_url =
                    item.coverArtUrl,
                is_owned =
                    if (item.isOwned) {
                        1L
                    } else {
                        0L
                    },
                is_wishlisted =
                    if (item.isWishlisted) {
                        1L
                    } else {
                        0L
                    },
                id =
                    item.id
            )

        refresh()
    }

    fun deleteMediaItem(
        id: String
    ) {
        database
            .mediaItemQueries
            .deleteById(id)

        refresh()
    }

    fun setOwned(
        id: String,
        owned: Boolean
    ) {
        database
            .mediaItemQueries
            .setOwned(
                is_owned =
                    if (owned) {
                        1L
                    } else {
                        0L
                    },
                id = id
            )

        refresh()
    }

    fun setWishlisted(
        id: String,
        wishlisted: Boolean
    ) {
        database
            .mediaItemQueries
            .setWishlisted(
                is_wishlisted =
                    if (wishlisted) {
                        1L
                    } else {
                        0L
                    },
                id = id
            )

        refresh()
    }

    fun moveWishlistItemToCollection(
        item: MediaItem
    ) {
        database
            .mediaItemQueries
            .updateItem(
                title =
                    item.title.trim(),
                subtitle =
                    item.subtitle.trim(),
                format =
                    item.format.databaseValue,
                release_year =
                    item.year?.toLong(),
                edition =
                    item.edition.nullIfBlank(),
                condition =
                    item.condition.nullIfBlank(),
                notes =
                    item.notes.nullIfBlank(),
                purchase_price =
                    item.purchasePrice,
                cover_art_url =
                    item.coverArtUrl,
                is_owned =
                    1L,
                is_wishlisted =
                    0L,
                id =
                    item.id
            )

        refresh()
    }

    private fun insertMediaItem(
        item: MediaItem
    ) {
        val nextOrder =
            database
                .mediaItemQueries
                .maxAddedOrder()
                .executeAsOne() + 1L

        database
            .mediaItemQueries
            .insertItem(
                id =
                    item.id,
                title =
                    item.title,
                subtitle =
                    item.subtitle,
                format =
                    item.format.databaseValue,
                release_year =
                    item.year?.toLong(),
                edition =
                    item.edition,
                condition =
                    item.condition,
                notes =
                    item.notes,
                purchase_price =
                    item.purchasePrice,
                date_added =
                    item.dateAdded,
                cover_art_url =
                    item.coverArtUrl,
                is_owned =
                    if (item.isOwned) {
                        1L
                    } else {
                        0L
                    },
                is_wishlisted =
                    if (item.isWishlisted) {
                        1L
                    } else {
                        0L
                    },
                added_order =
                    nextOrder
            )
    }

    private fun refresh() {
        _collectionItems.value =
            database
                .mediaItemQueries
                .selectCollection()
                .executeAsList()
                .map {
                    it.toMediaItem()
                }

        _wishlistItems.value =
            database
                .mediaItemQueries
                .selectWishlist()
                .executeAsList()
                .map {
                    it.toMediaItem()
                }
    }

    private fun generateId(): String {
        return "user-media-${Uuid.random()}"
    }

    private fun String?.nullIfBlank():
            String? {

        return this
            ?.trim()
            ?.ifBlank {
                null
            }
    }

    private fun com.ethanjohnson.flipside.db.Media_item
            .toMediaItem():
            MediaItem {

        return MediaItem(
            id =
                id,
            title =
                title,
            subtitle =
                subtitle,
            format =
                MediaFormat
                    .fromDatabaseValue(
                        format
                    )
                    ?: error(
                        "Unknown media format: $format"
                    ),
            year =
                release_year?.toInt(),
            edition =
                edition,
            condition =
                condition,
            notes =
                notes,
            purchasePrice =
                purchase_price,
            dateAdded =
                date_added,
            coverArtUrl =
                cover_art_url,
            isOwned =
                is_owned == 1L,
            isWishlisted =
                is_wishlisted == 1L
        )
    }
}