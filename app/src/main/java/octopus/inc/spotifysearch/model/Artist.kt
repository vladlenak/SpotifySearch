package octopus.inc.spotifysearch.model

data class Artist(
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)