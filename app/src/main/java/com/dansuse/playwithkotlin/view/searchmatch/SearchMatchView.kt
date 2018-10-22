package com.dansuse.playwithkotlin.view.searchmatch

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League

interface SearchMatchView{
  fun showLoading()
  fun hideLoading()
  fun showSearchResult(data: List<Event>)
  fun showErrorMessage(error: String)
}