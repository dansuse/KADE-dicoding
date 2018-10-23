package com.dansuse.playwithkotlin.view.matches

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League

interface MatchesView {
  fun showLoading()
  fun hideLoading()
  fun showLeagueList(data: List<League>)
  fun showEventList(data: List<Event>)
  fun showErrorMessage(error: String)
}