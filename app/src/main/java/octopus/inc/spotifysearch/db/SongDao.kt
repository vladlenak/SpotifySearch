package octopus.inc.spotifysearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import octopus.inc.spotifysearch.db.model.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM search_table")
    fun getSongs(): Single<List<Song>>

    @Query("DELETE FROM search_table")
    fun deleteAll()

    @Insert
    fun addSong(song: Song)
}