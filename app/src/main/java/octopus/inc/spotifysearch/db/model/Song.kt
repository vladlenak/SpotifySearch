package octopus.inc.spotifysearch.db.model

import androidx.room.*

@Entity(tableName = "search_table")
data class Song(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "artist")
    val artist: String,
    @ColumnInfo(name = "flow")
    val flow: Int
)