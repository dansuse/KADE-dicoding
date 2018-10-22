package com.dansuse.playwithkotlin.view.teamdetail

interface TeamOverviewView{
  fun showLoading()
  fun hideLoading()
  fun showTeamDescription(data: String)
  fun showErrorMessage(errorMessage: String)
}