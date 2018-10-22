package com.dansuse.playwithkotlin.presenter.teamdetail

import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.PlayersView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class PlayersPresenter(
    private val playersView: PlayersView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
){
  private var playersDisposable: Disposable? = null

  fun getPlayersByTeamId(teamId:String){
    playersView.showLoading()
    playersDisposable = theSportDBApiService
        .getPlayersByTeamId(teamId)
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              playersView.hideLoading()
              playersView.showPlayerList(it.player)
            },
            {
              playersView.hideLoading()
              playersView.showErrorMessage(it.message ?: "Terjadi error saat meminta data pemain dari api")
            }
        )
  }

  fun dispose() {
    playersDisposable?.dispose()
  }
}