package com.dansuse.playwithkotlin.view.teamdetail.overview

interface TeamOverviewView{
  fun showLoading()
  fun hideLoading()
  fun showTeamDescription(data: String)
  fun showErrorMessage(errorMessage: String)
}