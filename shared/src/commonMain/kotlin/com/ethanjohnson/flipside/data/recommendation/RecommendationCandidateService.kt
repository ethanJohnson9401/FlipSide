package com.ethanjohnson.flipside.data.recommendation

import com.ethanjohnson.flipside.data.search.MediaSearchRepository
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult

class RecommendationCandidateService(
    private val searchRepository: MediaSearchRepository
) {

    companion object {
        private const val MAX_CREATORS = 3
    }

    suspend fun discoverCandidates(
        collection: List<MediaItem>
    ): List<MediaSearchResult> {
        if (collection.isEmpty()) {
            return emptyList()
        }

        val creators =
            findTopCreators(
                collection
            )

        if (creators.isEmpty()) {
            return searchRepository
                .getCachedCandidates(
                    limit = 200
                )
        }

        val discovered =
            mutableListOf<MediaSearchResult>()

        creators.forEach { creator ->

            val query =
                buildArtistQuery(
                    creator
                )

            try {
                val results =
                    searchRepository.search(
                        query
                    )

                discovered.addAll(
                    results
                )
            } catch (exception: Exception) {
                println(
                    "FlipSide Recommendations: " +
                            "Could not load candidates for " +
                            "'$creator': ${exception.message}"
                )
            }
        }

        val cached =
            searchRepository
                .getCachedCandidates(
                    limit = 300
                )

        return (
                discovered + cached
                )
            .distinctBy { result ->
                buildString {
                    append(
                        result.source
                            .trim()
                            .lowercase()
                    )

                    append("::")

                    append(
                        result.externalId
                    )
                }
            }
    }

    private fun findTopCreators(
        collection: List<MediaItem>
    ): List<String> {

        val musicItems =
            collection.filter { item ->
                item.format == MediaFormat.VINYL ||
                        item.format == MediaFormat.CD ||
                        item.format == MediaFormat.CASSETTE
            }

        return musicItems
            .map {
                it.subtitle.trim()
            }
            .filter {
                it.isNotBlank()
            }
            .groupingBy {
                normalize(it)
            }
            .eachCount()
            .entries
            .sortedByDescending {
                it.value
            }
            .take(
                MAX_CREATORS
            )
            .mapNotNull { entry ->
                musicItems
                    .firstOrNull { item ->
                        normalize(
                            item.subtitle
                        ) == entry.key
                    }
                    ?.subtitle
                    ?.trim()
                    ?.takeIf {
                        it.isNotBlank()
                    }
            }
    }

    private fun buildArtistQuery(
        creator: String
    ): String {
        val escaped =
            creator.replace(
                "\"",
                ""
            )

        return "artist:\"$escaped\""
    }

    private fun normalize(
        value: String
    ): String {
        return value
            .trim()
            .lowercase()
    }
}