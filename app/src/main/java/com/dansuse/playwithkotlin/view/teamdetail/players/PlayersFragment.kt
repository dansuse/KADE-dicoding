package com.dansuse.playwithkotlin.view.teamdetail.players

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Player
import com.dansuse.playwithkotlin.presenter.teamdetail.PlayersPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.players.playerdetail.PlayerDetailActivity
import com.dansuse.playwithkotlin.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.*


class PlayersFragment : Fragment(), AnkoComponent<Context>, PlayersView {

  companion object {
    const val EXTRA_TEAM_ID = "extra_team_id"
  }

  private lateinit var progressBar: ProgressBar
  private lateinit var textErrorMessage: TextView
  private lateinit var listPlayer: RecyclerView

  private var players: MutableList<Player> = mutableListOf()
  private lateinit var adapter: PlayersAdapter
  private lateinit var presenter: PlayersPresenter

  private var teamId:String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.getString(EXTRA_TEAM_ID)?.let {
      teamId = it
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Log.d("tes123", "masuk onActivityCreated PlayersFragment")
    adapter = PlayersAdapter(players) {
      context?.startActivity<PlayerDetailActivity>("id" to it.playerId)
    }
    listPlayer.adapter = adapter

    presenter = PlayersPresenter(this, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
    presenter.getPlayersByTeamId(teamId?:"")

  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui){
    frameLayout {
      lparams(width = matchParent, height = matchParent)

      textErrorMessage = textView(){
        gravity = Gravity.CENTER
      }
      listPlayer = recyclerView {
        id = R.id.list_player
        layoutManager = LinearLayoutManager(ctx)
      }.lparams(width = matchParent, height = matchParent)
      progressBar = progressBar()
    }
  }

  override fun showLoading() {
    progressBar.visible()
  }

  override fun hideLoading() {
    progressBar.invisible()
  }

  override fun showPlayerList(data: List<Player>) {
    Log.d("tes123", "showPlayerList : ${data.size}")
    hideLoading()
    textErrorMessage.invisible()
    listPlayer.visible()
    players.clear()
    players.addAll(data)
    adapter.notifyDataSetChanged()
  }

  override fun showErrorMessage(errorMessage: String) {
    hideLoading()
    listPlayer.invisible()
    textErrorMessage.visible()
    textErrorMessage.text = errorMessage
  }
}