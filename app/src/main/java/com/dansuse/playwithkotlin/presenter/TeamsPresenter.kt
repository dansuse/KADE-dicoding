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
  private var searchResultDisposable: Disposable? = null
  var leagueDisposable: Disposable? = null

  open fun getLeagueList() {
    leagueDisposable = theSportDBApiService.getAllLeagues()
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            { leagueResponse ->
              view.hideLoading()
              view.showLeagueList(leagueResponse.leagues)
            },
            { error ->
              view.hideLoading()
              view.showErrorMessage(error.message
                  ?: "Terjadi kesalahan saat mencoba mengambil data")
            }
        )
  }

  fun getSearchResult(query:String){
    view.showLoading()
    searchResultDisposable = theSportDBApiService
        .searchTeam(query)
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
    leagueDisposable?.dispose()
    teamDisposable?.dispose()
    searchResultDisposable?.dispose()
  }
}
