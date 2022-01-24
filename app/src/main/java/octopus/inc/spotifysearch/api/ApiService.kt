package octopus.inc.spotifysearch.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("search")
    fun search(
    @Header("Authorization") authHeader: String,
    @Query("q") search: String,
    @Query("type") type: String,
    @Query("include_external") includeExternal: String,
    @Query("limit") limit: String,
    @Query("offset") offset: String
    ): Single<SpotifyResponse>
}