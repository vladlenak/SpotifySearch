package octopus.inc.spotifysearch.model

data class TrackSearchResponse (
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