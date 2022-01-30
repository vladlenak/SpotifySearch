package octopus.inc.spotifysearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import octopus.inc.spotifysearch.SpotifySearchApplication
import octopus.inc.spotifysearch.activity.LoginActivity.Companion.getSpotifyToken
import octopus.inc.spotifysearch.api.model.TrackResponse

class TrackViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val track = MutableLiveData<TrackResponse>()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getTrack(trackId: String) {
        getSpotifyToken()?.let { token ->
            SpotifySearchApplication.api?.getTrack(token, trackId, "UA")?.let {
                compositeDisposable.add(it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        track.value = it
                    }, {}))
            }
        }
    }

    companion object {
        private const val TAG = "SongDetailViewModel"
    }
}