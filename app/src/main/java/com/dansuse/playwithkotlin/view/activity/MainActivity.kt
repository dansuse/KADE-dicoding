package com.dansuse.playwithkotlin.view.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.R.color.colorAccent
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Team
import com.dansuse.playwithkotlin.presenter.TeamsPresenter
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.view.TeamsView
import com.dansuse.playwithkotlin.view.adapter.TeamsAdapter
import com.dansuse.playwithkotlin.visible
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MainActivity : AppCompatActivity(), TeamsView {

  private lateinit var listTeam: RecyclerView
  private lateinit var progressBar: ProgressBar
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var spinner: Spinner

  private var teams: MutableList<Team> = mutableListOf()
  private lateinit var presenter: TeamsPresenter
  private lateinit var adapter: TeamsAdapter
  private lateinit var leagueName: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



  }

  override fun showLoading() {
    progressBar.visible()
  }

  override fun hideLoading() {
    progressBar.invisible()
  }

  override fun showTeamList(data: List<Team>) {
    swipeRefresh.isRefreshing = false
    teams.clear()
    teams.addAll(data)
    adapter.notifyDataSetChanged()
  }
}
