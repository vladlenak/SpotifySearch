package octopus.inc.spotifysearch.viewmodel

import android.app.Application
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

    private val compositeDisposable = CompositeDisposable()
    private val songRepository = SongRepository.get()

    val track = MutableLiveData<Song>()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun search(search: String) {
        getSpotifyToken()?.let { token ->
            api?.search(token, search, "track", "audio", "10", "0")?.let {
                compositeDisposable.add(it
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ trackSearchResponse ->
                        val items = trackSearchResponse.tracks.items

                        for (item in items) {
                            getSong(item, 1)?.let { song ->
                                track.value = song
                            }
                        }
                    }, {

                    }))
            }

            api?.search(token, search, "track", "audio", "10", "10")?.let {
                compositeDisposable.add(it
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ trackSearchResponse ->
                        val items = trackSearchResponse.tracks.items

                        for (item in items) {
                            getSong(item, 2)?.let { song ->
                                track.value = song
                            }
                        }
                    }, {}))
            }

        }
    }

    private fun getSong(item: Item, flowNumber: Int): Song? {
        val id = item.id
        val name = item.name
        val artists = item.artists
        var artistStr: String? = null

        for (artist in artists) {
            if (artistStr == null) {
                artistStr = artist.name
            } else {
                artistStr += ", ${artist.name}"
            }
        }

        return artistStr?.let {
            Song(id, name, it, flowNumber)
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

    companion object {
        private const val TAG = "SearchViewModel"
    }
}