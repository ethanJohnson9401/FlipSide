package com.ethanjohnson.flipside.data.search

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MusicBrainzSearchServiceTest {

    private var client: HttpClient? = null

    @AfterTest
    fun tearDown() {
        client?.close()
    }

    @Test
    fun mapsMusicBrainzReleaseToSearchResult() =
        runTest {
            client =
                HttpClient(
                    MockEngine {
                        respond(
                            content = RESPONSE,
                            headers =
                                headersOf(
                                    HttpHeaders.ContentType,
                                    ContentType
                                        .Application
                                        .Json
                                        .toString()
                                )
                        )
                    }
                ) {
                    install(
                        ContentNegotiation
                    ) {
                        json(
                            Json {
                                ignoreUnknownKeys =
                                    true
                            }
                        )
                    }
                }

            val service =
                MusicBrainzSearchService(
                    client = client!!
                )

            val results =
                service.search(
                    "kind of blue"
                )

            assertEquals(
                1,
                results.size
            )

            val result =
                results.first()

            assertEquals(
                "release-1",
                result.externalId
            )

            assertEquals(
                "Kind of Blue",
                result.title
            )

            assertEquals(
                "Miles Davis",
                result.subtitle
            )

            assertEquals(
                1959,
                result.year
            )

            assertEquals(
                "vinyl",
                result.format
                    ?.databaseValue
            )

            assertEquals(
                "US • Official • 12\"",
                result.edition
            )

            assertEquals(
                "MusicBrainz",
                result.source
            )
        }

    @Test
    fun returnsNullFormatForUnsupportedReleaseFormat() =
        runTest {
            client =
                HttpClient(
                    MockEngine {
                        respond(
                            content =
                                UNSUPPORTED_FORMAT_RESPONSE,
                            headers =
                                headersOf(
                                    HttpHeaders.ContentType,
                                    ContentType
                                        .Application
                                        .Json
                                        .toString()
                                )
                        )
                    }
                ) {
                    install(
                        ContentNegotiation
                    ) {
                        json(
                            Json {
                                ignoreUnknownKeys =
                                    true
                            }
                        )
                    }
                }

            val service =
                MusicBrainzSearchService(
                    client = client!!
                )

            val results =
                service.search(
                    "digital release"
                )

            assertEquals(
                1,
                results.size
            )

            assertNull(
                results.first().format
            )
        }

    @Test
    fun deduplicatesResultsByExternalId() =
        runTest {
            client =
                HttpClient(
                    MockEngine {
                        respond(
                            content =
                                DUPLICATE_RESPONSE,
                            headers =
                                headersOf(
                                    HttpHeaders.ContentType,
                                    ContentType
                                        .Application
                                        .Json
                                        .toString()
                                )
                        )
                    }
                ) {
                    install(
                        ContentNegotiation
                    ) {
                        json(
                            Json {
                                ignoreUnknownKeys =
                                    true
                            }
                        )
                    }
                }

            val service =
                MusicBrainzSearchService(
                    client = client!!
                )

            val results =
                service.search(
                    "duplicate"
                )

            assertEquals(
                1,
                results.size
            )

            assertNotNull(
                results.first()
            )
        }

    private companion object {
        const val RESPONSE =
            """
            {
              "releases": [
                {
                  "id": "release-1",
                  "title": "Kind of Blue",
                  "date": "1959-08-17",
                  "country": "US",
                  "status": "Official",
                  "packaging": "12\"",
                  "artist-credit": [
                    {
                      "name": "Miles Davis",
                      "joinphrase": ""
                    }
                  ],
                  "media": [
                    {
                      "format": "12\" Vinyl"
                    }
                  ]
                }
              ]
            }
            """

        const val UNSUPPORTED_FORMAT_RESPONSE =
            """
            {
              "releases": [
                {
                  "id": "release-2",
                  "title": "Digital Album",
                  "date": "2024",
                  "artist-credit": [
                    {
                      "name": "Test Artist",
                      "joinphrase": ""
                    }
                  ],
                  "media": [
                    {
                      "format": "File"
                    }
                  ]
                }
              ]
            }
            """

        const val DUPLICATE_RESPONSE =
            """
            {
              "releases": [
                {
                  "id": "release-3",
                  "title": "Duplicate Album",
                  "artist-credit": [
                    {
                      "name": "Test Artist",
                      "joinphrase": ""
                    }
                  ],
                  "media": [
                    {
                      "format": "CD"
                    }
                  ]
                },
                {
                  "id": "release-3",
                  "title": "Duplicate Album",
                  "artist-credit": [
                    {
                      "name": "Test Artist",
                      "joinphrase": ""
                    }
                  ],
                  "media": [
                    {
                      "format": "CD"
                    }
                  ]
                }
              ]
            }
            """
    }
}