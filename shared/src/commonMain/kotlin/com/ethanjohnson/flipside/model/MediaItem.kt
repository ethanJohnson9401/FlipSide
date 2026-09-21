package com.ethanjohnson.flipside.model

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val format: MediaFormat,
    val year: Int? = null
)