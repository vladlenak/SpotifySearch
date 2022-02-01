package octopus.inc.spotifysearch.viewmodel

import android.app.Application
import android.util.Log
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
import octopus.inc.spotifysearch.api.model.TrackSearchResponse
import octopus.inc.spotifysearch.db.SongRepository
import octopus.inc.spotifysearch.db.model.Song
import java.lang.Thread.currentThread
import java.lang.Thread.sleep
import java.util.concurrent.Executors

class TrackListViewModel(application: Application) : AndroidViewModel(application), MyProgressDialog.Callbacks {

    private lateinit var compositeDisposable: CompositeDisposable
    private val songRepository = SongRepository.get()

    val track = MutableLiveData<Song>()
    private var totalTracks = 0

    private val dialog = MyProgressDialog()

    private val thread1 = Schedulers.from(Executors.newSingleThreadExecutor())
    private val thread2 = Schedulers.from(Executors.newSingleThreadExecutor())

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun search(search: String, childFragmentManager: FragmentManager) {
        compositeDisposable = CompositeDisposable()
        dialog.setCallbacks(this)
        dialog.show(childFragmentManager, MyProgressDialog.TAG)
        getSpotifyToken()?.let { token ->
            val observable1 = api?.search(token, search, "track", "audio", "10", "0")
            val observable2 = api?.search(token, search, "track", "audio", "10", "10")
            val observable3 = api?.search(token, search, "track", "audio", "10", "20")
            val observable4 = api?.search(token, search, "track", "audio", "10", "30")

            compositeDisposable.add(
                Observable.zip(observable1, observable2, BiFunction { t1, t2 ->
                    totalTracks = t1.tracks.total.toInt()

                    val list = ArrayList<Song>()
                    list.addAll(convertToSong(t1, 1))
                    list.addAll(convertToSong(t2, 2))
                    Log.d(TAG, "thread ${currentThread().id}")
                    sleep(1000)
                    list
                })
                    .subscribeOn(thread1)
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
                                    val list = ArrayList<Song>()
                                    list.addAll(convertToSong(t1, 3))
                                    list.addAll(convertToSong(t2, 4))
                                    Log.d(TAG, "thread ${currentThread().name}")
                                    sleep(5000)
                                    list
                                })
                                    .subscribeOn(thread2)
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

    private fun convertToSong(trackSearchResponse: TrackSearchResponse, flowNumber: Int): ArrayList<Song> {
        val items = trackSearchResponse.tracks.items
        val list = ArrayList<Song>()

        for (item in items) {
            getSong(item, flowNumber)?.let { song ->
                list.add(song)
            }
        }

        return list
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
        private const val TAG = "TrackListViewModel"
    }

    override fun onClickCancelButton() {
        Log.d(TAG, "onClickCancelButton: ")
//        thread1.shutdown()
//        thread2.shutdown()
        compositeDisposable.dispose()
        dialog.dismiss()
    }
}