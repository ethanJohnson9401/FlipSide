package com.ethanjohnson.flipside.data.search

import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaSearchResult

class FakeMediaSearchService : MediaSearchService {

    override suspend fun search(
        query: String
    ): List<MediaSearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }

        val results = listOf(
            MediaSearchResult(
                externalId = "music-abbey-road",
                title = "Abbey Road",
                subtitle = "The Beatles",
                year = 1969,
                format = MediaFormat.VINYL,
                source = "Fake"
            ),

            MediaSearchResult(
                externalId = "music-dark-side",
                title = "The Dark Side of the Moon",
                subtitle = "Pink Floyd",
                year = 1973,
                format = MediaFormat.VINYL,
                source = "Fake"
            ),

            MediaSearchResult(
                externalId = "movie-blade-runner",
                title = "Blade Runner",
                subtitle = "Ridley Scott",
                year = 1982,
                format = MediaFormat.BLURAY,
                source = "Fake"
            ),

            MediaSearchResult(
                externalId = "game-chrono-trigger",
                title = "Chrono Trigger",
                subtitle = "Square",
                year = 1995,
                format = MediaFormat.GAME,
                source = "Fake"
            )
        )

        return results.filter { result ->
            result.title.contains(
                query,
                ignoreCase = true
            ) ||
                    result.subtitle.contains(
                        query,
                        ignoreCase = true
                    )
        }
    }
}