package octopus.inc.spotifysearch.model

data class TrackResponse (
    val album: Album,
    val artists: List<Artist>,
    val discNumber: Long,
    val durationMS: Long,
    val explicit: Boolean,
    val externalIDS: ExternalIDS,
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val isLocal: Boolean,
    val isPlayable: Boolean,
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
