package octopus.inc.spotifysearch

import android.app.Application
import octopus.inc.spotifysearch.api.ApiService
import octopus.inc.spotifysearch.api.RetrofitClient
import octopus.inc.spotifysearch.db.SongRepository

class SpotifySearchApplication : Application() {

    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1/"

        val api: ApiService? by lazy {
            RetrofitClient.getRetrofit(BASE_URL)?.create(ApiService::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        SongRepository.initialize(this)
    }
}