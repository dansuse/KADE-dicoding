package com.dansuse.playwithkotlin.view.matchdetail

import com.dansuse.playwithkotlin.model.Event

interface MatchDetailView {
  fun showLoading()
  fun hideLoading()
  fun showEventDetail(event: Event)
  fun showErrorMessage(error: String)
}