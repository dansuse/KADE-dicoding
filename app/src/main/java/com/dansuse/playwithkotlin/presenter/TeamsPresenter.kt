package com.dansuse.playwithkotlin.presenter

import android.support.test.espresso.idling.CountingIdlingResource
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.repository.TheSportDBApi
import com.dansuse.playwithkotlin.view.CoroutineContextProvider
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
    private val gson: Gson,
    private val context:CoroutineContextProvider,
    private val idlingRes:CountingIdlingResource){
  fun getTeamList(league: String?){
      idlingRes.increment()
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
    async(context.main){
        val data: Deferred<TeamResponse> = bg {
            gson.fromJson(apiRepository.doRequest(TheSportDBApi.getTeams(league)),
                    TeamResponse::class.java)
        }
        view.hideLoading()
        view.showTeamList(data.await().teams)
        idlingRes.decrement()
    }
  }
}
