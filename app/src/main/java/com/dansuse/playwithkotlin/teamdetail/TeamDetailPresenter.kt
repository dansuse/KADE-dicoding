package com.dansuse.playwithkotlin.teamdetail

import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.repository.TheSportDBApi
import com.google.gson.Gson
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamDetailPresenter(private val view: TeamDetailView,
                          private val apiRepository: ApiRepository,
                          private val gson:Gson){
    fun getTeamDetail(teamId:String){
        view.showLoading()
//        doAsync {
//            val data = gson.fromJson(
//                     apiRepository.doRequest(
//                            TheSportDBApi.getTeamDetail(teamId)),
//                     TeamResponse::class.java)
//            uiThread {
//                view.hideLoading()
//                view.showTeamDetail(data.teams)
//            }
//        }

        async(UI){
            val data: Deferred<TeamResponse> = bg{
                gson.fromJson(
                        apiRepository.doRequest(
                                TheSportDBApi.getTeamDetail(teamId)),
                        TeamResponse::class.java)
            }
            view.hideLoading()
            view.showTeamDetail(data.await().teams)
        }
    }
}