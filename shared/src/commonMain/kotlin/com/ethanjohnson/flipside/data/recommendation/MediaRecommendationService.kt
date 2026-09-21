package com.ethanjohnson.flipside.data.recommendation

import com.ethanjohnson.flipside.model.MediaItem
import com.ethanjohnson.flipside.model.MediaSearchResult

interface MediaRecommendationService {

    suspend fun recommendations(
        collection: List<MediaItem>,
        wishlist: List<MediaItem>,
        candidates: List<MediaSearchResult>
    ): List<MediaSearchResult>
}