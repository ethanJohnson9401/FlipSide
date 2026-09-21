package com.ethanjohnson.flipside.model

data class MediaSearchResult(
    val externalId: String,
    val title: String,
    val subtitle: String,
    val year: Int? = null,
    val format: MediaFormat? = null,
    val edition: String? = null,
    val coverArtUrl: String? = null,
    val source: String
)