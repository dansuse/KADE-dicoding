package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.TeamDetailView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class TeamDetailPresenter(
    private val view: TeamDetailView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler) {

  private var teamDetailDisposable: Disposable? = null

  fun getTeamDetail(teamId: String) {
    view.showLoading()

    teamDetailDisposable = theSportDBApiService
        .getTeamDetail(teamId.toInt())
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              view.hideLoading()
              view.showTeamDetail(it.teams)
            },
            {
              view.hideLoading()
              view.showErrorMessage(it.message ?: "Terjadi error saat load team detail")
            }
        )
  }

  fun dispose() {
    teamDetailDisposable?.dispose()
  }
}