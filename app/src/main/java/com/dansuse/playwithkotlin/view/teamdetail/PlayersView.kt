package com.dansuse.playwithkotlin.view.teamdetail

import com.dansuse.playwithkotlin.model.Player

interface PlayersView{
  fun showLoading()
  fun hideLoading()
  fun showPlayerList(data: List<Player>)
  fun showErrorMessage(errorMessage: String)
}