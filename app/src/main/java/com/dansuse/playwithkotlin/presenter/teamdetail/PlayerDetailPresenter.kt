package com.dansuse.playwithkotlin.presenter.teamdetail

import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.players.playerdetail.PlayerDetailView
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class PlayerDetailPresenter(
    private val view: PlayerDetailView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
){
  private var playersDisposable: Disposable? = null

  fun getPlayerDetailByPlayerId(playerId:String){
    view.showLoading()
    playersDisposable = theSportDBApiService
        .getPlayerDetail(playerId)
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            {
              view.hideLoading()
              if(it.players.isNotEmpty()){
                view.showPlayerDetail(it.players[0])
              }
            },
            {
              view.hideLoading()
              view.showErrorMessage(it.message ?: "Terjadi error saat meminta data detail player dari api")
            }
        )
  }

  fun dispose() {
    playersDisposable?.dispose()
  }
}