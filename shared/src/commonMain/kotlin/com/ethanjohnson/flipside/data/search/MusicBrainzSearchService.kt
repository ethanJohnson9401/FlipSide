package com.ethanjohnson.flipside.data.search

import com.ethanjohnson.flipside.model.MediaFormat
import com.ethanjohnson.flipside.model.MediaSearchResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class MusicBrainzSearchService(
    private val client: HttpClient = createHttpClient()
) : MediaSearchService {

    override suspend fun search(
        query: String
    ): List<MediaSearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }

        val response =
            client.get(
                "https://musicbrainz.org/ws/2/release/"
            ) {
                parameter(
                    "query",
                    query.trim()
                )

                parameter(
                    "fmt",
                    "json"
                )

                parameter(
                    "limit",
                    25
                )

                header(
                    HttpHeaders.UserAgent,
                    USER_AGENT
                )
            }

        val body =
            response.body<MusicBrainzReleaseSearchResponse>()

        return body.releases
            .map { release ->
                release.toMediaSearchResult()
            }
            .distinctBy { it.externalId }
    }

    companion object {
        private const val USER_AGENT =
            "FlipSide/0.1 (https://github.com/ethanJohnson9401/FlipSide)"
    }
}

private fun createHttpClient():
        HttpClient {

    return HttpClient {
        install(
            ContentNegotiation
        ) {
            json(
                Json {
                    ignoreUnknownKeys =
                        true

                    isLenient =
                        true
                }
            )
        }
    }
}

private fun MusicBrainzRelease
        .toMediaSearchResult():
        MediaSearchResult {

    val artist =
        artistCredit
            ?.joinToString("") { credit ->

                buildString {
                    append(
                        credit.name
                            ?: credit.artist
                                ?.name
                            ?: ""
                    )

                    credit.joinPhrase
                        ?.let {
                            append(it)
                        }
                }
            }
            ?.trim()
            .orEmpty()

    return MediaSearchResult(
        externalId =
            id,

        title =
            title,

        subtitle =
            artist,

        year =
            date
                ?.take(4)
                ?.toIntOrNull(),

        format =
            detectMediaFormat(),

        edition =
            buildEdition(),

        coverArtUrl =
            "https://coverartarchive.org/release/$id/front-250",

        source =
            "MusicBrainz"
    )
}

private fun MusicBrainzRelease
        .detectMediaFormat():
        MediaFormat? {

    val formats =
        media
            .orEmpty()
            .mapNotNull { medium ->
                medium.format
                    ?.lowercase()
            }

    return when {
        formats.any { format ->
            "vinyl" in format
        } -> {
            MediaFormat.VINYL
        }

        formats.any { format ->
            format == "cd" ||
                    "compact disc" in format
        } -> {
            MediaFormat.CD
        }

        formats.any { format ->
            "cassette" in format
        } -> {
            MediaFormat.CASSETTE
        }

        else -> {
            null
        }
    }
}

private fun MusicBrainzRelease
        .buildEdition():
        String? {

    val parts =
        buildList {
            country
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                    add(it)
                }

            status
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                    add(it)
                }

            packaging
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                    add(it)
                }
        }

    return parts
        .joinToString(
            " • "
        )
        .ifBlank {
            null
        }
}

@Serializable
private data class MusicBrainzReleaseSearchResponse(
    val releases:
    List<MusicBrainzRelease> =
        emptyList()
)

@Serializable
private data class MusicBrainzRelease(
    val id: String,

    val title: String,

    val date: String? =
        null,

    val country: String? =
        null,

    val status: String? =
        null,

    val packaging: String? =
        null,

    @SerialName(
        "artist-credit"
    )
    val artistCredit:
    List<MusicBrainzArtistCredit>? =
        null,

    val media:
    List<MusicBrainzMedium>? =
        null
)

@Serializable
private data class MusicBrainzArtistCredit(
    val name: String? =
        null,

    @SerialName(
        "joinphrase"
    )
    val joinPhrase: String? =
        null,

    val artist:
    MusicBrainzArtist? =
        null
)

@Serializable
private data class MusicBrainzArtist(
    val id: String? =
        null,

    val name: String? =
        null
)

@Serializable
private data class MusicBrainzMedium(
    val format: String? =
        null
)