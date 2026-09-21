package com.ethanjohnson.flipside.data.search

import com.ethanjohnson.flipside.model.MediaSearchResult

interface MediaSearchService {

    suspend fun search(
        query: String
    ): List<MediaSearchResult>
}