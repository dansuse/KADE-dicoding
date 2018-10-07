package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.repository.TheSportDBApi
import com.dansuse.playwithkotlin.view.TeamsView
import com.google.gson.Gson
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamsPresenter (
    private val view: TeamsView,
    private val apiRepository: ApiRepository,
    private val gson: Gson){
  fun getTeamList(league: String?){
    view.showLoading()
//    doAsync {
//      val data = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getTeams(league)),
//          TeamResponse::class.java)
//
//      uiThread {
//        view.hideLoading()
//        view.showTeamList(data.teams)
//      }
//    }
    async(UI){
        val data: Deferred<TeamResponse> = bg {
            gson.fromJson(apiRepository.doRequest(TheSportDBApi.getTeams(league)),
                    TeamResponse::class.java)
        }
        view.hideLoading()
        view.showTeamList(data.await().teams)
    }
  }
}
