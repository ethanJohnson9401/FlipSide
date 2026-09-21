package com.ethanjohnson.flipside.data

import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem

object FakeMediaData {

    val collectionItems = listOf(
        MediaItem(
            id = "dark-side-of-the-moon",
            title = "The Dark Side of the Moon",
            subtitle = "Pink Floyd",
            format = MediaFormat.VINYL,
            year = 1973,
            edition = "US pressing • Harvest Records",
            condition = "Very Good+",
            notes = "Original sleeve included. Minor wear on outer jacket.",
            purchasePrice = 24.99,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "abbey-road",
            title = "Abbey Road",
            subtitle = "The Beatles",
            format = MediaFormat.VINYL,
            year = 1969,
            edition = "Capitol Records reissue",
            condition = "Very Good",
            notes = "Clean vinyl with light jacket wear.",
            purchasePrice = 19.50,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "blade-runner",
            title = "Blade Runner",
            subtitle = "Ridley Scott",
            format = MediaFormat.VHS,
            year = 1982,
            edition = "Warner Home Video VHS",
            condition = "Very Good",
            notes = "Case has light shelf wear.",
            purchasePrice = 8.00,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "alien",
            title = "Alien",
            subtitle = "Ridley Scott",
            format = MediaFormat.VHS,
            year = 1979,
            edition = "CBS/Fox Video release",
            condition = "Good",
            notes = "Tape plays well. Case has moderate wear.",
            purchasePrice = 6.50,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "chrono-trigger",
            title = "Chrono Trigger",
            subtitle = "Square",
            format = MediaFormat.GAME,
            year = 1995,
            edition = "Super Nintendo • North American release",
            condition = "Very Good",
            notes = "Loose cartridge. Label is clean.",
            purchasePrice = 139.99,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "metroid-prime",
            title = "Metroid Prime",
            subtitle = "Nintendo",
            format = MediaFormat.GAME,
            year = 2002,
            edition = "Nintendo GameCube • Black Label",
            condition = "Very Good+",
            notes = "Complete with case and manual.",
            purchasePrice = 29.99,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "rumours",
            title = "Rumours",
            subtitle = "Fleetwood Mac",
            format = MediaFormat.CD,
            year = 1977,
            edition = "Warner Bros. CD reissue",
            condition = "Near Mint",
            notes = "Disc and booklet are both very clean.",
            purchasePrice = 5.99,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "the-thing",
            title = "The Thing",
            subtitle = "John Carpenter",
            format = MediaFormat.DVD,
            year = 1982,
            edition = "Collector's Edition DVD",
            condition = "Very Good",
            notes = "Includes original insert.",
            purchasePrice = 9.99,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "wish-you-were-here",
            title = "Wish You Were Here",
            subtitle = "Pink Floyd",
            format = MediaFormat.CASSETTE,
            year = 1975,
            edition = "Columbia cassette release",
            condition = "Good",
            notes = "Tape shell has light cosmetic wear.",
            purchasePrice = 7.50,
            dateAdded = null,
            isOwned = true
        ),
        MediaItem(
            id = "2001-a-space-odyssey",
            title = "2001: A Space Odyssey",
            subtitle = "Stanley Kubrick",
            format = MediaFormat.BLURAY,
            year = 1968,
            edition = "Warner Bros. Blu-ray",
            condition = "Near Mint",
            notes = "Case, disc, and artwork are all clean.",
            purchasePrice = 12.99,
            dateAdded = null,
            isOwned = true
        )
    )

    val recentlyAdded = listOf(
        collectionItems.first { it.id == "dark-side-of-the-moon" },
        collectionItems.first { it.id == "blade-runner" },
        collectionItems.first { it.id == "chrono-trigger" },
        collectionItems.first { it.id == "abbey-road" },
        collectionItems.first { it.id == "the-thing" }
    )

    val recommendations = listOf(
        collectionItems.first { it.id == "wish-you-were-here" },
        collectionItems.first { it.id == "alien" },
        collectionItems.first { it.id == "chrono-trigger" },
        collectionItems.first { it.id == "rumours" },
        collectionItems.first { it.id == "metroid-prime" }
    )

    val wishlistItems = listOf(
        MediaItem(
            id = "akira-bluray",
            title = "Akira",
            subtitle = "Katsuhiro Otomo",
            format = MediaFormat.BLURAY,
            year = 1988,
            edition = "25th Anniversary Blu-ray",
            condition = null,
            notes = "Looking for a clean complete copy.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        ),
        MediaItem(
            id = "silent-hill-2",
            title = "Silent Hill 2",
            subtitle = "Konami",
            format = MediaFormat.GAME,
            year = 2001,
            edition = "PlayStation 2 • North American release",
            condition = null,
            notes = "Prefer complete with manual.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        ),
        MediaItem(
            id = "in-rainbows",
            title = "In Rainbows",
            subtitle = "Radiohead",
            format = MediaFormat.VINYL,
            year = 2007,
            edition = "Vinyl LP",
            condition = null,
            notes = "Looking for a clean pressing.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        ),
        MediaItem(
            id = "the-shining-vhs",
            title = "The Shining",
            subtitle = "Stanley Kubrick",
            format = MediaFormat.VHS,
            year = 1980,
            edition = "Warner Home Video VHS",
            condition = null,
            notes = "Prefer original case.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        ),
        MediaItem(
            id = "resident-evil-gamecube",
            title = "Resident Evil",
            subtitle = "Capcom",
            format = MediaFormat.GAME,
            year = 2002,
            edition = "Nintendo GameCube",
            condition = null,
            notes = "Black label preferred.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        ),
        MediaItem(
            id = "animals",
            title = "Animals",
            subtitle = "Pink Floyd",
            format = MediaFormat.VINYL,
            year = 1977,
            edition = "Vinyl LP",
            condition = null,
            notes = "Original or early pressing preferred.",
            purchasePrice = null,
            dateAdded = null,
            isOwned = false,
            isWishlisted = true
        )
    )
}