package octopus.inc.spotifysearch.api

import androidx.room.ColumnInfo
import androidx.room.Entity

data class SpotifyResponse (
    val tracks: Tracks
)

data class Tracks (
    val href: String,
    val items: List<Item>,
    val limit: Long,
    val next: String,
    val offset: Long,
    val previous: Any? = null,
    val total: Long
)


data class Item (

    val album: Album,
    val artists: List<Artist>,
    val availableMarkets: List<String>,
    val discNumber: Long,
    val durationMS: Long,
    val explicit: Boolean,
    val externalIDS: ExternalIDS,
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val isLocal: Boolean,
    val name: String,
    val popularity: Long,
    val previewURL: String,
    val trackNumber: Long,
    val type: String,
    val uri: String
)

data class Album (
    val albumType: String,
    val artists: List<Artist>,
    val availableMarkets: List<String>,
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    val totalTracks: Long,
    val type: String,
    val uri: String
)

data class Artist (
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ExternalUrls (
    val spotify: String
)

data class Image (
    val height: Long,
    val url: String,
    val width: Long
)

data class ExternalIDS (
    val isrc: String
)
