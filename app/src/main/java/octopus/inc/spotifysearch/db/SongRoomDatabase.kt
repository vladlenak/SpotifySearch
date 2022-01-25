package octopus.inc.spotifysearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import octopus.inc.spotifysearch.model.Song

@Database(entities = [Song::class], version = 1, exportSchema = true)
abstract class SongRoomDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        const val DATABASE_NAME = "song_database"

        @Volatile
        private var INSTANCE: SongRoomDatabase? = null

        fun getDatabase(context: Context): SongRoomDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongRoomDatabase::class.java,
                    "note_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}