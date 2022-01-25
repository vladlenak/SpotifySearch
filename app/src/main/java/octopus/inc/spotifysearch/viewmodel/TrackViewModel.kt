package octopus.inc.spotifysearch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SpotifySearchApplication
import octopus.inc.spotifysearch.activity.LoginActivity.Companion.SPOTIFY_ACCESS_TOKEN

class TrackViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getTrack(trackId: String) {
        compositeDisposable.add(SpotifySearchApplication.api.getTrack(SPOTIFY_ACCESS_TOKEN, trackId, "UA")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "Track: ${it.toString()}")
            }, {}))
    }

    companion object {
        private const val TAG = "SongDetailViewModel"
    }
}