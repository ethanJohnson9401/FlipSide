package com.ethanjohnson.flipside.model

enum class MediaFormat(
    val displayName: String
) {
    VINYL("Vinyl"),
    CD("CD"),
    CASSETTE("Cassette"),
    VHS("VHS"),
    DVD("DVD"),
    BLURAY("Blu-ray"),
    UHD_BLURAY("4K Blu-ray"),
    GAME("Game"),
    BOOK("Book")
}