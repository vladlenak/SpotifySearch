package octopus.inc.spotifysearch.model

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.google.android.material.circularreveal.CircularRevealHelper
import java.util.*

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