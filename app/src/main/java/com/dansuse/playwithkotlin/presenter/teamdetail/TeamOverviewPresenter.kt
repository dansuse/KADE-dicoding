package com.dansuse.playwithkotlin.presenter.teamdetail

import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.overview.TeamOverviewView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class TeamOverviewPresenter(
    private val view: TeamOverviewView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
){
  private var teamOverviewDisposable: Disposable? = null

  fun getTeamDetailByTeamId(teamId:String){
    view.showLoading()
    teamOverviewDisposable = theSportDBApiService
        .getTeamDetail(teamId.toInt())
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              view.hideLoading()
              if(it.teams.isNotEmpty()){
                view.showTeamDescription(it.teams[0].teamDescription ?: "")
              }
            },
            {
              view.hideLoading()
              view.showErrorMessage(it.message ?: "Terjadi error saat load team overview")
            }
        )
  }

  fun dispose() {
    teamOverviewDisposable?.dispose()
  }
}