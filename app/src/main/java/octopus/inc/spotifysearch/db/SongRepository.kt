package octopus.inc.spotifysearch.db

import android.content.Context
import androidx.room.Room
import io.reactivex.Flowable
import io.reactivex.Single
import octopus.inc.spotifysearch.model.Song
import java.util.concurrent.Executors

class SongRepository private constructor(context: Context) {

    private val database: SongRoomDatabase = Room.databaseBuilder(
        context.applicationContext,
        SongRoomDatabase::class.java,
        SongRoomDatabase.DATABASE_NAME
    ).build()
    private val noteDao = database.songDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getSongs(): Single<List<Song>> = noteDao.getSongs()

    fun addSong(song: Song) {
        executor.execute {
            noteDao.addSong(song)
        }
    }

    fun deleteAll() {
        executor.execute {
            noteDao.deleteAll()
        }
    }

    companion object {
        private var INSTANCE: SongRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = SongRepository(context)
        }

        fun get(): SongRepository = INSTANCE
            ?: throw IllegalStateException("Repository must be initialized")
    }
}