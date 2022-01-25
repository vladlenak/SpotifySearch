package octopus.inc.spotifysearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SpotifySearchApplication.Companion.api
import octopus.inc.spotifysearch.activity.LoginActivity.Companion.SPOTIFY_ACCESS_TOKEN
import octopus.inc.spotifysearch.model.TrackSearchResponse
import octopus.inc.spotifysearch.db.SongRepository
import octopus.inc.spotifysearch.model.Song

class TrackListViewModel(application: Application) : AndroidViewModel(application) {

    private var callbacks: Callbacks? = null
    private val compositeDisposable = CompositeDisposable()
    private val songRepository = SongRepository.get()

    override fun onCleared() {
        compositeDisposable.dispose()
        callbacks = null
        super.onCleared()
    }

    fun setCallbacks(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    fun search(search: String) {
        val requestList = listOf(
            api.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "0"),
            api.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "10"),
            api.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "20"),
            api.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "30")
        )

        compositeDisposable.add(requestList[0]
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onResponse(it, 1)
            }, {}))

        compositeDisposable.add(requestList[1]
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onResponse(it, 2)
            }, {}))
    }

    private fun onResponse(response: TrackSearchResponse, numberOfThread: Int) {
        val itemSize = response.tracks.items.size

        var i = 0
        while (i < itemSize) {
            val id = response.tracks.items[i].id
            val songName = response.tracks.items[i].name
            var artistsString = ""

            var j = 0
            while (j < response.tracks.items[i].artists.size) {
                artistsString += " ${response.tracks.items[i].artists[j].name}"
                j++
            }

            val song = Song(id, songName, artistsString, numberOfThread)
            callbacks?.addSongToAdapter(song)

            Log.d(TAG, "Href ${response.tracks.items[i].availableMarkets}")

            i++
        }
    }

    fun addSongToDB(song: Song) {
        songRepository.addSong(song)
    }

    fun deleteAllFromDB() {
        songRepository.deleteAll()
    }

    fun getSongsFromDB(): Single<List<Song>> {
        return songRepository.getSongs()
    }

    interface Callbacks {
        fun addSongToAdapter(song: Song)
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}