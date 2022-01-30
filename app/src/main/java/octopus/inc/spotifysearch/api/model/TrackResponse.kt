package octopus.inc.spotifysearch.api.model

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