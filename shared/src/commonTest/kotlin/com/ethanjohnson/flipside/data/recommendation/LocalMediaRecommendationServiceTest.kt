package com.ethanjohnson.flipside.data.recommendation

import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalMediaRecommendationServiceTest {

    private val service =
        LocalMediaRecommendationService()

    @Test
    fun excludesOwnedItemWithSameFormat() =
        runTest {
            val collection =
                listOf(
                    mediaItem(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val candidates =
                listOf(
                    searchResult(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val recommendations =
                service.recommendations(
                    collection = collection,
                    wishlist = emptyList(),
                    candidates = candidates
                )

            assertTrue(
                recommendations.isEmpty()
            )
        }

    @Test
    fun keepsSameTitleWhenFormatDiffers() =
        runTest {
            val collection =
                listOf(
                    mediaItem(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val candidates =
                listOf(
                    searchResult(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.CD
                    )
                )

            val recommendations =
                service.recommendations(
                    collection = collection,
                    wishlist = emptyList(),
                    candidates = candidates
                )

            assertEquals(
                1,
                recommendations.size
            )

            assertEquals(
                MediaFormat.CD,
                recommendations.first().format
            )
        }

    @Test
    fun ranksMatchingArtistAboveUnrelatedArtist() =
        runTest {
            val collection =
                listOf(
                    mediaItem(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val candidates =
                listOf(
                    searchResult(
                        title = "Bitches Brew",
                        subtitle = "Miles Davis",
                        format = MediaFormat.CD
                    ),
                    searchResult(
                        title = "Blue Train",
                        subtitle = "John Coltrane",
                        format = MediaFormat.CD
                    )
                )

            val recommendations =
                service.recommendations(
                    collection = collection,
                    wishlist = emptyList(),
                    candidates = candidates
                )

            assertEquals(
                "Bitches Brew",
                recommendations.first().title
            )
        }

    @Test
    fun excludesItemAlreadyInWishlist() =
        runTest {
            val collection =
                listOf(
                    mediaItem(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val wishlist =
                listOf(
                    mediaItem(
                        title = "Bitches Brew",
                        subtitle = "Miles Davis",
                        format = MediaFormat.CD
                    )
                )

            val candidates =
                listOf(
                    searchResult(
                        title = "Bitches Brew",
                        subtitle = "Miles Davis",
                        format = MediaFormat.CD
                    )
                )

            val recommendations =
                service.recommendations(
                    collection = collection,
                    wishlist = wishlist,
                    candidates = candidates
                )

            assertTrue(
                recommendations.isEmpty()
            )
        }

    @Test
    fun excludesCandidateWithoutPhysicalFormat() =
        runTest {
            val collection =
                listOf(
                    mediaItem(
                        title = "Kind of Blue",
                        subtitle = "Miles Davis",
                        format = MediaFormat.VINYL
                    )
                )

            val candidates =
                listOf(
                    searchResult(
                        title = "Bitches Brew",
                        subtitle = "Miles Davis",
                        format = null
                    )
                )

            val recommendations =
                service.recommendations(
                    collection = collection,
                    wishlist = emptyList(),
                    candidates = candidates
                )

            assertTrue(
                recommendations.isEmpty()
            )
        }

    @Test
    fun returnsEmptyWhenCollectionIsEmpty() =
        runTest {
            val recommendations =
                service.recommendations(
                    collection = emptyList(),
                    wishlist = emptyList(),
                    candidates =
                        listOf(
                            searchResult(
                                title = "Kind of Blue",
                                subtitle = "Miles Davis",
                                format = MediaFormat.CD
                            )
                        )
                )

            assertTrue(
                recommendations.isEmpty()
            )
        }

    private fun mediaItem(
        title: String,
        subtitle: String,
        format: MediaFormat
    ): MediaItem {
        return MediaItem(
            id = "item-$title-$format",
            title = title,
            subtitle = subtitle,
            format = format,
            year = 1959
        )
    }

    private fun searchResult(
        title: String,
        subtitle: String,
        format: MediaFormat?
    ): MediaSearchResult {
        return MediaSearchResult(
            externalId = "release-$title-$format",
            title = title,
            subtitle = subtitle,
            year = 1959,
            format = format,
            source = "Test"
        )
    }
}