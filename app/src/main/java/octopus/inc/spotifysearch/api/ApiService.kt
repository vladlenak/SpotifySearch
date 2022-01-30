package octopus.inc.spotifysearch.api

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import octopus.inc.spotifysearch.api.model.TrackResponse
import octopus.inc.spotifysearch.api.model.TrackSearchResponse
import retrofit2.http.*

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
    ): Observable<TrackSearchResponse>

    @Headers("Content-Type: application/json")
    @GET("tracks/{id}")
    fun getTrack(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @Query("market") market: String
    ): Single<TrackResponse>
}