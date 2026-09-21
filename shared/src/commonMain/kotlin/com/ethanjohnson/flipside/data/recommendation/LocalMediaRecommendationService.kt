package com.ethanjohnson.flipside.data.recommendation

import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult
import kotlin.math.abs

class LocalMediaRecommendationService :
    MediaRecommendationService {

    override suspend fun recommendations(
        collection: List<MediaItem>,
        wishlist: List<MediaItem>,
        candidates: List<MediaSearchResult>
    ): List<MediaSearchResult> {

        if (collection.isEmpty()) {
            return emptyList()
        }

        val ownedKeys =
            collection
                .map {
                    mediaKey(
                        title = it.title,
                        subtitle = it.subtitle
                    )
                }
                .toSet()

        val wishlistKeys =
            wishlist
                .map {
                    mediaKey(
                        title = it.title,
                        subtitle = it.subtitle
                    )
                }
                .toSet()

        return candidates
            .filter { candidate ->
                mediaKey(
                    title = candidate.title,
                    subtitle = candidate.subtitle
                ) !in ownedKeys
            }
            .map { candidate ->
                candidate to scoreCandidate(
                    candidate = candidate,
                    collection = collection,
                    wishlistKeys = wishlistKeys
                )
            }
            .filter {
                it.second > 0
            }
            .sortedByDescending {
                it.second
            }
            .map {
                it.first
            }
            .distinctBy {
                mediaKey(
                    title = it.title,
                    subtitle = it.subtitle
                )
            }
            .take(10)
    }

    private fun scoreCandidate(
        candidate: MediaSearchResult,
        collection: List<MediaItem>,
        wishlistKeys: Set<String>
    ): Int {
        var score = 0

        val candidateArtist =
            normalize(candidate.subtitle)

        collection.forEach { owned ->
            val ownedArtist =
                normalize(owned.subtitle)

            if (
                candidateArtist.isNotBlank() &&
                candidateArtist == ownedArtist
            ) {
                score += 100
            }

            if (
                candidate.format != null &&
                candidate.format == owned.format
            ) {
                score += 15
            }

            if (
                candidate.year != null &&
                owned.year != null
            ) {
                val difference =
                    abs(
                        candidate.year -
                                owned.year
                    )

                score += when {
                    difference <= 2 -> 15
                    difference <= 5 -> 10
                    difference <= 10 -> 5
                    else -> 0
                }
            }
        }

        val candidateKey =
            mediaKey(
                title = candidate.title,
                subtitle = candidate.subtitle
            )

        if (
            candidateKey in wishlistKeys
        ) {
            score -= 50
        }

        return score
    }

    private fun mediaKey(
        title: String,
        subtitle: String
    ): String {
        return buildString {
            append(
                normalize(title)
            )

            append("::")

            append(
                normalize(subtitle)
            )
        }
    }

    private fun normalize(
        value: String
    ): String {
        return value
            .trim()
            .lowercase()
    }
}