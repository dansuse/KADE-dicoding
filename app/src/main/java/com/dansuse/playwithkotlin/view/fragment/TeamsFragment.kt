package com.dansuse.playwithkotlin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Spinner
import com.dansuse.playwithkotlin.model.Team
import com.dansuse.playwithkotlin.presenter.TeamsPresenter
import com.dansuse.playwithkotlin.view.TeamsView
import com.dansuse.playwithkotlin.view.adapter.TeamsAdapter
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

class TeamsFragment : Fragment(), AnkoComponent<Context>, TeamsView {

  private lateinit var listTeam: RecyclerView
  private lateinit var progressBar: ProgressBar
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var spinner: Spinner

  private var teams: MutableList<Team> = mutableListOf()
  private lateinit var presenter: TeamsPresenter
  private lateinit var adapter: TeamsAdapter
  private lateinit var leagueName: String

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

  }

  override fun createView(ui: AnkoContext<Context>): View {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showLoading() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hideLoading() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showTeamList(data: List<Team>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}