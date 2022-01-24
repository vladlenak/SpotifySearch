package octopus.inc.spotifysearch

import android.app.Application
import octopus.inc.spotifysearch.api.ApiService
import octopus.inc.spotifysearch.api.RetrofitClient
import octopus.inc.spotifysearch.db.SongRepository

class SpotifySearchApplication : Application() {

    companion object {
        private var _api: ApiService? = null
        val api: ApiService by lazy { _api!! }

        private const val BASE_URL = "https://api.spotify.com/v1/"
    }

    override fun onCreate() {
        super.onCreate()

        _api = RetrofitClient.getRetrofit(BASE_URL)
            .create(ApiService::class.java)

        SongRepository.initialize(this)
    }
}