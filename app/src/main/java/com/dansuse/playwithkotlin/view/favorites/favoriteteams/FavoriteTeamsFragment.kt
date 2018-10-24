package com.dansuse.playwithkotlin.view.favorites.favoriteteams

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dansuse.playwithkotlin.EspressoIdlingResource
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.database.database
import com.dansuse.playwithkotlin.model.FavoriteTeam
import com.dansuse.playwithkotlin.view.teamdetail.TeamDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class FavoriteTeamsFragment : Fragment(), AnkoComponent<Context> {

  private var favoriteTeams: MutableList<FavoriteTeam> = mutableListOf()
  private lateinit var adapter: FavoriteTeamsAdapter
  private lateinit var listTeam: RecyclerView
  private lateinit var swipeRefresh: SwipeRefreshLayout

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    adapter = FavoriteTeamsAdapter(favoriteTeams) {
      requireContext().startActivity<TeamDetailActivity>("id" to "${it.teamId}")
    }

    listTeam.adapter = adapter
    showFavorite()
    swipeRefresh.onRefresh {
      favoriteTeams.clear()
      showFavorite()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui) {
    linearLayout {
      lparams(width = matchParent, height = matchParent)
      topPadding = dip(16)
      leftPadding = dip(16)
      rightPadding = dip(16)

      swipeRefresh = swipeRefreshLayout {
        id = R.id.swipe_refresh_favorite_team
        setColorSchemeResources(R.color.colorAccent,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        listTeam = recyclerView {
          id = R.id.list_favorite_team
          lparams(width = matchParent, height = matchParent)
          layoutManager = LinearLayoutManager(ctx)
        }
      }
    }
  }

  private fun showFavorite() {
    EspressoIdlingResource.mCountingIdlingResource.increment()
    context?.database?.use {
      swipeRefresh.isRefreshing = false
      val result = select(FavoriteTeam.TABLE_FAVORITE)
      val favorite = result.parseList(classParser<FavoriteTeam>())
      favoriteTeams.addAll(favorite)
      adapter.notifyDataSetChanged()
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }
}