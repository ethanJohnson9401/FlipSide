package com.ethanjohnson.flipside.model

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val format: MediaFormat,
    val year: Int? = null,
    val edition: String? = null,
    val condition: String? = null,
    val notes: String? = null,
    val purchasePrice: Double? = null,
    val dateAdded: Long? = null,
    val coverArtUrl: String? = null,
    val isOwned: Boolean = true,
    val isWishlisted: Boolean = false
)