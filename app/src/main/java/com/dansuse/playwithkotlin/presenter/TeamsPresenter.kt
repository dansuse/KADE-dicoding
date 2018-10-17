package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teams.TeamsView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class TeamsPresenter(
    private val view: TeamsView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
) {

  private var teamDisposable: Disposable? = null

  fun getTeamList(league: String) {
    view.showLoading()
    teamDisposable = theSportDBApiService.getTeams(league)
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              view.hideLoading()
              view.showTeamList(it.teams)
            },
            {
              view.hideLoading()
              view.showErrorMessage(it.message ?: "Terjadi error saat load data team")
            }
        )
  }

  fun dispose() {
    teamDisposable?.dispose()
  }
}
