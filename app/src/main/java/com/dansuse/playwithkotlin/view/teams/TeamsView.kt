package com.dansuse.playwithkotlin.view.teams

import com.dansuse.playwithkotlin.model.Team

interface TeamsView {
  fun showLoading()
  fun hideLoading()
  fun showTeamList(data: List<Team>)
  fun showErrorMessage(errorMessage: String)
}