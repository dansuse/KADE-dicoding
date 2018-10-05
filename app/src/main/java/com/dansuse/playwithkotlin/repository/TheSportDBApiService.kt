package com.dansuse.playwithkotlin.repository

import com.dansuse.playwithkotlin.BuildConfig
import com.dansuse.playwithkotlin.model.EventResponse
import com.dansuse.playwithkotlin.model.LeagueResponse
import com.dansuse.playwithkotlin.model.TeamResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheSportDBApiService {
  companion object {
    const val MODE_PAST_15_EVENTS = "eventspastleague.php"
    const val MODE_NEXT_15_EVENTS = "eventsnextleague.php"
    fun create():TheSportDBApiService{
      val retrofit = Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl(BuildConfig.BASE_URL)
          .build()
      return retrofit.create(TheSportDBApiService::class.java)
    }
  }

  @GET("api/v1/json/" + BuildConfig.TSDB_API_KEY + "/{mode}")
  fun get15EventsByLeagueId(@Path("mode") mode: String, @Query("id") leagueId: Int): Observable<EventResponse>

  @GET("api/v1/json/" + BuildConfig.TSDB_API_KEY + "/lookupteam.php")
  fun getTeamDetail(@Query("id") teamId: Int): Single<TeamResponse>

  @GET("api/v1/json/" + BuildConfig.TSDB_API_KEY + "/lookupevent.php")
  fun getEventDetail(@Query("id") eventId: Int): Observable<EventResponse>

  @GET("api/v1/json/" + BuildConfig.TSDB_API_KEY + "/all_leagues.php")
  fun getAllLeagues(): Observable<LeagueResponse>
}