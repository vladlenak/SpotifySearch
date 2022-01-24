package octopus.inc.spotifysearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import octopus.inc.spotifysearch.model.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM search_table")
    fun getSongs(): Single<List<Song>>

    @Query("DELETE FROM search_table")
    fun deleteAll()

    @Insert
    fun addSong(song: Song)
}