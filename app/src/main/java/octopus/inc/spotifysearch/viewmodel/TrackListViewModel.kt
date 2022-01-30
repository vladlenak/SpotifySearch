package octopus.inc.spotifysearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SpotifySearchApplication.Companion.api
import octopus.inc.spotifysearch.activity.LoginActivity.Companion.getSpotifyToken
import octopus.inc.spotifysearch.api.model.Item
import octopus.inc.spotifysearch.db.SongRepository
import octopus.inc.spotifysearch.db.model.Song
import octopus.inc.spotifysearch.api.model.TrackSearchResponse

class TrackListViewModel(application: Application) : AndroidViewModel(application) {

    private var callbacks: Callbacks? = null
    private val compositeDisposable = CompositeDisposable()
    private val songRepository = SongRepository.get()

    val tracksLiveData = MutableLiveData<TrackSearchResponse>()

    override fun onCleared() {
        compositeDisposable.dispose()
        callbacks = null
        super.onCleared()
    }

    fun setCallbacks(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    fun trackSource(search: String, offset: String): Single<TrackSearchResponse>? {
        getSpotifyToken()?.let { token ->
            return api?.search(token, search, "track", "audio", "10", offset)
        }

        return null
    }

    fun search(search: String) {
        val currentList = ArrayList<Song>()
//        val requestList = listOf(
//            api?.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "0"),
//            api?.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "10"),
//            api?.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "20"),
//            api?.search(SPOTIFY_ACCESS_TOKEN, search, "track", "audio", "10", "30")
//        )

        trackSource(search, "0")?.let {
            compositeDisposable.add(it
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {}))
        }


        getSpotifyToken()?.let { token ->
            api?.search(token, search, "track", "audio", "10", "10")?.let {
                compositeDisposable.add(it
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        onResponse(it, 2)
                    }, {}))
            }

        }
    }

    fun getSong(item: Item, flowNumber: Int): Song? {
        val id = item.id
        val name = item.name
        val artists = item.artists
        var artistStr: String? = null

        for (artist in artists) {
            if (artistStr == null) {
                artistStr = artist.name
            } else {
                artistStr += ", $artist"
            }
        }

        return artistStr?.let {
            Song(id, name, it, flowNumber)
        }
    }

    private fun onResponse(trackSearchResponse: TrackSearchResponse, numberOfThread: Int) {
        val itemSize = trackSearchResponse.tracks.items.size

        var i = 0
        while (i < itemSize) {
            val id = trackSearchResponse.tracks.items[i].id
            val songName = trackSearchResponse.tracks.items[i].name
            var artistsString = ""

            var j = 0
            while (j < trackSearchResponse.tracks.items[i].artists.size) {
                artistsString += " ${trackSearchResponse.tracks.items[i].artists[j].name}"
                j++
            }

            val song = Song(id, songName, artistsString, numberOfThread)
            callbacks?.addSongToAdapter(song)

            Log.d(TAG, "Href ${trackSearchResponse.tracks.items[i].availableMarkets}")

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