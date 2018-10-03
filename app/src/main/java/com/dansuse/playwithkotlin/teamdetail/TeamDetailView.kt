package com.dansuse.playwithkotlin.teamdetail

import com.dansuse.playwithkotlin.model.Team

interface TeamDetailView{
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(data: List<Team>)
}