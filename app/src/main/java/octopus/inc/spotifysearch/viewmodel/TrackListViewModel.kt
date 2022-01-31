package octopus.inc.spotifysearch.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.MyProgressDialog
import octopus.inc.spotifysearch.SpotifySearchApplication.Companion.api
import octopus.inc.spotifysearch.activity.LoginActivity.Companion.getSpotifyToken
import octopus.inc.spotifysearch.api.model.Item
import octopus.inc.spotifysearch.db.SongRepository
import octopus.inc.spotifysearch.db.model.Song

class TrackListViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val songRepository = SongRepository.get()

    val track = MutableLiveData<Song>()
    var totalTracks = 0

    private val dialog = MyProgressDialog()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun search(search: String, childFragmentManager: FragmentManager) {
        dialog.show(childFragmentManager, MyProgressDialog.TAG)
        getSpotifyToken()?.let { token ->
            val observable1 = api?.search(token, search, "track", "audio", "10", "0")
            val observable2 = api?.search(token, search, "track", "audio", "10", "10")
            val observable3 = api?.search(token, search, "track", "audio", "10", "20")
            val observable4 = api?.search(token, search, "track", "audio", "10", "30")

            compositeDisposable.add(
                Observable.zip(observable1, observable2, BiFunction { t1, t2 ->
                    val list: MutableList<Song> = ArrayList()
                    totalTracks = t1.tracks.total.toInt()

                    val items1 = t1.tracks.items

                    for (item in items1) {
                        getSong(item, 1)?.let { song ->
                            list.add(song)
                        }
                    }

                    val items2 = t2.tracks.items

                    for (item in items2) {
                        getSong(item, 2)?.let { song ->
                            list.add(song)
                        }
                    }
                    list
                })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        for (song in it) {
                            track.value = song
                        }
                    }, {

                    }, {
                        if (totalTracks > 19) {
                            compositeDisposable.add(
                                Observable.zip(observable3, observable4, BiFunction { t1, t2 ->
                                    val list: MutableList<Song> = ArrayList()
                                    totalTracks = t1.tracks.total.toInt()

                                    val items1 = t1.tracks.items

                                    for (item in items1) {
                                        getSong(item, 3)?.let { song ->
                                            list.add(song)
                                        }
                                    }

                                    val items2 = t2.tracks.items

                                    for (item in items2) {
                                        getSong(item, 4)?.let { song ->
                                            list.add(song)
                                        }
                                    }
                                    list
                                })
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        for (song in it) {
                                            track.value = song
                                        }
                                    }, {

                                    }, {
                                        dialog.dismiss()
                                    }, {

                                    }))
                        } else {
                            dialog.dismiss()
                        }
                    }, {

                    }))
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