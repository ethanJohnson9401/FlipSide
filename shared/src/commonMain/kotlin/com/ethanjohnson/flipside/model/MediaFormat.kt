package com.ethanjohnson.flipside.model

enum class MediaFormat(
    val displayName: String,
    val databaseValue: String
) {
    VINYL(
        displayName = "Vinyl",
        databaseValue = "vinyl"
    ),

    CD(
        displayName = "CD",
        databaseValue = "cd"
    ),

    CASSETTE(
        displayName = "Cassette",
        databaseValue = "cassette"
    ),

    VHS(
        displayName = "VHS",
        databaseValue = "vhs"
    ),

    DVD(
        displayName = "DVD",
        databaseValue = "dvd"
    ),

    BLURAY(
        displayName = "Blu-ray",
        databaseValue = "bluray"
    ),

    UHD_BLURAY(
        displayName = "4K Blu-ray",
        databaseValue = "uhd_bluray"
    ),

    GAME(
        displayName = "Game",
        databaseValue = "game"
    ),

    BOOK(
        displayName = "Book",
        databaseValue = "book"
    );

    companion object {
        fun fromDatabaseValue(
            value: String
        ): MediaFormat? {
            return entries.firstOrNull {
                it.databaseValue == value
            }
        }
    }
}