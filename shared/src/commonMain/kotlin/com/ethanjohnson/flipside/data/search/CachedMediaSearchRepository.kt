package com.ethanjohnson.flipside.data.search

import com.ethanjohnson.flipside.db.FlipSideDatabase
import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaSearchResult
import kotlin.time.Clock

class CachedMediaSearchRepository(
    private val database: FlipSideDatabase,
    private val searchService: MediaSearchService
) : MediaSearchRepository {

    companion object {
        private const val CACHE_DURATION_MS =
            7L * 24L * 60L * 60L * 1000L
    }

    override suspend fun search(
        query: String
    ): List<MediaSearchResult> {
        val normalizedQuery =
            normalizeQuery(query)

        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        deleteExpiredEntries()

        val cachedResults =
            loadCachedResults(
                normalizedQuery
            )

        if (cachedResults.isNotEmpty()) {
            println(
                "FlipSide Search: CACHE HIT for '$normalizedQuery'"
            )

            return cachedResults
        }

        println(
            "FlipSide Search: CACHE MISS for '$normalizedQuery'"
        )

        val results =
            searchService.search(
                normalizedQuery
            )

        saveResults(
            query = normalizedQuery,
            results = results
        )

        return results
    }

    override fun getCachedCandidates(
        limit: Long
    ): List<MediaSearchResult> {
        deleteExpiredEntries()

        return database
            .mediaSearchCacheQueries
            .selectRecentCandidates(
                limit
            )
            .executeAsList()
            .map { row ->
                MediaSearchResult(
                    externalId =
                        row.external_id,
                    title =
                        row.title,
                    subtitle =
                        row.subtitle,
                    year =
                        row.release_year
                            ?.toInt(),
                    format =
                        row.format?.let {
                            MediaFormat
                                .fromDatabaseValue(
                                    it
                                )
                        },
                    edition =
                        row.edition,
                    coverArtUrl =
                        row.cover_art_url,
                    source =
                        row.source
                )
            }
            .distinctBy { result ->
                buildString {
                    append(
                        result.source.lowercase()
                    )

                    append("::")

                    append(
                        result.externalId
                    )
                }
            }
    }

    override fun clearCache() {
        database
            .mediaSearchCacheQueries
            .deleteAll()
    }

    private fun loadCachedResults(
        query: String
    ): List<MediaSearchResult> {
        return database
            .mediaSearchCacheQueries
            .selectByQuery(query)
            .executeAsList()
            .map { row ->
                MediaSearchResult(
                    externalId =
                        row.external_id,
                    title =
                        row.title,
                    subtitle =
                        row.subtitle,
                    year =
                        row.release_year
                            ?.toInt(),
                    format =
                        row.format?.let {
                            MediaFormat
                                .fromDatabaseValue(
                                    it
                                )
                        },
                    edition =
                        row.edition,
                    coverArtUrl =
                        row.cover_art_url,
                    source =
                        row.source
                )
            }
    }

    private fun saveResults(
        query: String,
        results: List<MediaSearchResult>
    ) {
        val now =
            Clock.System
                .now()
                .toEpochMilliseconds()

        database
            .mediaSearchCacheQueries
            .deleteByQuery(query)

        results.forEach { result ->
            database
                .mediaSearchCacheQueries
                .insertResult(
                    cache_id =
                        createCacheId(
                            query = query,
                            result = result
                        ),
                    query =
                        query,
                    external_id =
                        result.externalId,
                    title =
                        result.title,
                    subtitle =
                        result.subtitle,
                    release_year =
                        result.year?.toLong(),
                    format =
                        result.format
                            ?.databaseValue,
                    edition =
                        result.edition,
                    cover_art_url =
                        result.coverArtUrl,
                    source =
                        result.source,
                    cached_at =
                        now
                )
        }
    }

    private fun deleteExpiredEntries() {
        val now =
            Clock.System
                .now()
                .toEpochMilliseconds()

        val cutoff =
            now - CACHE_DURATION_MS

        database
            .mediaSearchCacheQueries
            .deleteExpired(
                cutoff
            )
    }

    private fun normalizeQuery(
        query: String
    ): String {
        return query
            .trim()
            .lowercase()
    }

    private fun createCacheId(
        query: String,
        result: MediaSearchResult
    ): String {
        return buildString {
            append(query)
            append("::")
            append(result.source)
            append("::")
            append(result.externalId)
        }
    }
}