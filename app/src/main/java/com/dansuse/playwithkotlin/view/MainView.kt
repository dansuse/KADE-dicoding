package com.dansuse.playwithkotlin.view

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League

interface MainView {
  fun showLoading()
  fun hideLoading()
  fun showLeagueList(data: List<League>)
  fun showEventList(data: List<Event>)
  fun showErrorMessage(error: String)
}