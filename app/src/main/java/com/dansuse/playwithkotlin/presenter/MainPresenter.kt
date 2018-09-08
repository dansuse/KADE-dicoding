package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.repository.TheSportDBApi
import com.dansuse.playwithkotlin.view.MainView
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainPresenter (
    private val view: MainView,
    private val apiRepository: ApiRepository,
    private val gson: Gson){
  fun getTeamList(league: String?){
    view.showLoading()
    doAsync {
      val data = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getTeams(league)),
          TeamResponse::class.java)

      uiThread {
        view.hideLoading()
        view.showTeamList(data.teams)
      }
    }
  }
}
