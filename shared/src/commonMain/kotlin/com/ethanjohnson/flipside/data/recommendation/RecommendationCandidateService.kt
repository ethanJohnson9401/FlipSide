package com.ethanjohnson.flipside.data.recommendation

import com.ethanjohnson.flipside.data.search.MediaSearchRepository
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult
import kotlinx.coroutines.delay
import com.ethanjohnson.flipside.model.MediaFormat

class RecommendationCandidateService(
    private val searchRepository: MediaSearchRepository
) {

    companion object {
        private const val MAX_CREATORS = 3

        /*
         * MusicBrainz asks clients to stay around
         * one request per second.
         */
        private const val SEARCH_DELAY_MS = 1100L
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

        creators.forEachIndexed {
                index,
                creator ->

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

            /*
             * Don't delay after the final query.
             */
            if (
                index <
                creators.lastIndex
            ) {
                delay(
                    SEARCH_DELAY_MS
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