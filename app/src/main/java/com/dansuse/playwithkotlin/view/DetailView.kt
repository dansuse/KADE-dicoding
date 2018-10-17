package com.dansuse.playwithkotlin.view

import com.dansuse.playwithkotlin.model.Event

interface DetailView {
  fun showLoading()
  fun hideLoading()
  fun showEventDetail(event: Event)
  fun showErrorMessage(error: String)
}