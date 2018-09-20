package com.dansuse.playwithkotlin.repository

import android.net.Uri
import com.dansuse.playwithkotlin.BuildConfig

object TheSportDBApi {
  fun getTeams(league:String?):String{
    return getCommonUriBuilder()
        .appendPath("search_all_teams.php")
        .appendQueryParameter("l", league)
        .build()
        .toString()
  }

  fun getAllLeagues():String{
    return getCommonUriBuilder()
        .appendPath("all_leagues.php")
        .build()
        .toString()
  }

  fun getNext15EventsByLeagueId(leagueId:String):String{
    return getCommonUriBuilder()
        .appendPath("eventsnextleague.php")
        .appendQueryParameter("id", leagueId)
        .build()
        .toString()
  }

  fun getPast15EventsByLeagueId(leagueId: Int):String{
    return getCommonUriBuilder()
        .appendPath("eventspastleague.php")
        .appendQueryParameter("id", leagueId.toString())
        .build()
        .toString()
  }

  fun getCommonUriBuilder():Uri.Builder{
    return Uri.parse(BuildConfig.BASE_URL).buildUpon()
        .appendPath("api")
        .appendPath("v1")
        .appendPath("json")
        .appendPath(BuildConfig.TSDB_API_KEY)
  }
}