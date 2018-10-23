package com.dansuse.playwithkotlin.view.teamdetail.players.playerdetail

import com.dansuse.playwithkotlin.model.Player

interface PlayerDetailView{
  fun showLoading()
  fun hideLoading()
  fun showPlayerDetail(data: Player)
  fun showErrorMessage(errorMessage: String)
}