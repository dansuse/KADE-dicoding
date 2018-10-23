package com.dansuse.playwithkotlin.presenter.teamdetail

import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.players.PlayersView
import com.dansuse.playwithkotlin.view.teamdetail.TeamDetailView
import com.dansuse.playwithkotlin.view.teamdetail.overview.TeamOverviewView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class TeamDetailPresenter(
    private val view: TeamDetailView,
    private val teamOverviewView: TeamOverviewView,
    private val playersView: PlayersView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler) {

  private var teamDetailDisposable: Disposable? = null


  fun getTeamDetail(teamId: String) {
    view.showLoading()
//    teamOverviewView.showLoading()

    teamDetailDisposable = theSportDBApiService
        .getTeamDetail(teamId.toInt())
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              view.hideLoading()
              view.showTeamDetail(it.teams)
//              teamOverviewView.hideLoading()
//              if(it.teams.isNotEmpty()){
//                teamOverviewView.showTeamDescription(it.teams[0].teamDescription?:"")
//              }
            },
            {
              view.hideLoading()
              view.showErrorMessage(it.message ?: "Terjadi error saat load team detail")
              //teamOverviewView.hideLoading()
              //teamOverviewView.showErrorMessage(it.message ?: "Terjadi error saat load team detail")
            }
        )
  }

  fun dispose() {
    teamDetailDisposable?.dispose()
  }
}