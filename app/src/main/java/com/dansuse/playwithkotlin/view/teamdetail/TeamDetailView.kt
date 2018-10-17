package com.dansuse.playwithkotlin.view.teamdetail

import com.dansuse.playwithkotlin.model.Team

interface TeamDetailView {
  fun showLoading()
  fun hideLoading()
  fun showTeamDetail(data: List<Team>)
  fun showErrorMessage(errorMessage: String)
}