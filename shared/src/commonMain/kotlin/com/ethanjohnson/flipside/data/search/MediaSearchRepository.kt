package com.ethanjohnson.flipside.data.search

import com.ethanjohnson.flipside.model.MediaSearchResult

interface MediaSearchRepository {

    suspend fun search(
        query: String
    ): List<MediaSearchResult>

    fun getCachedCandidates(
        limit: Long = 100
    ): List<MediaSearchResult>

    fun clearCache()
}