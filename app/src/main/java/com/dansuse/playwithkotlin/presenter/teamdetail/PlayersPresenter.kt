package com.dansuse.playwithkotlin.presenter.teamdetail

import android.util.Log
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.players.PlayersView
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
    Log.d("tes123", "getPlayersByTeamId() $teamId")
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
              Log.d("tes123", "getPlayersByTeamId() ${it.message}")
              playersView.hideLoading()
              playersView.showErrorMessage(it.message ?: "Terjadi error saat meminta data pemain dari api")
            }
        )
  }

  fun dispose() {
    playersDisposable?.dispose()
  }
}