package com.dansuse.playwithkotlin.view

import com.dansuse.playwithkotlin.model.Team

interface MainView{
  fun showLoading()
  fun hideLoading()
  fun showTeamList(data: List<Team>)
}