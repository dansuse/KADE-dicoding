package com.dansuse.playwithkotlin.view.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.view.favorites.FavoriteContainerFragment
import com.dansuse.playwithkotlin.view.favorites.favoritematches.FavoriteMatchesFragment
import com.dansuse.playwithkotlin.view.matches.MatchContainerFragment
import com.dansuse.playwithkotlin.view.matches.MatchesFragment
import com.dansuse.playwithkotlin.view.teams.TeamsFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView

class MainActivity : AppCompatActivity() {

  private lateinit var bottomNavigation: BottomNavigationView
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val states = arrayOf(
        intArrayOf(android.R.attr.state_checked), // enabled
        intArrayOf(-android.R.attr.state_checked) // disabled
    )

    val colors = intArrayOf(Color.WHITE, Color.LTGRAY)
    val colorStateList = ColorStateList(states, colors)

    relativeLayout {
      lparams(width = matchParent, height = matchParent) {
        margin = dip(0)
      }
      padding = dip(0)

      frameLayout {
        lparams(width = matchParent, height = matchParent)
        id = R.id.main_container
      }.lparams(width = matchParent, height = matchParent) {
        topOf(R.id.bottom_navigation_bar)
      }

      bottomNavigation = bottomNavigationView {
        id = R.id.bottom_navigation_bar
        inflateMenu(R.menu.bottom_navigation_view)
        itemBackgroundResource = R.color.colorPrimary
        itemTextColor = colorStateList
        itemIconTintList = colorStateList
        padding = dip(0)
      }.lparams(width = matchParent, height = wrapContent) {
        alignParentBottom()
        margin = dip(0)
      }
    }
    bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.action_matches -> {
          //isPrevMatchMode = true
          //presenter.get15EventsByLeagueId(leagueId ?:"", isPrevMatchMode)
          loadMatchContainerFragment(savedInstanceState)
          //loadMatchesFragment(savedInstanceState, true)
          true
        }
        R.id.action_teams -> {
          //isPrevMatchMode = false
          //presenter.get15EventsByLeagueId(leagueId?:"", isPrevMatchMode)
          //loadMatchesFragment(savedInstanceState, false)
          loadTeamsFragment(savedInstanceState)
          true
        }
        R.id.action_favorites -> {
          loadFavoriteContainerFragment(savedInstanceState)
          //loadFavoritesFragment(savedInstanceState)
          true
        }
        else -> false
      }
    }
    bottomNavigation.selectedItemId = R.id.action_matches
  }

  private fun loadMatchContainerFragment(savedInstanceState: Bundle?) {
    val matchContainerFragment = MatchContainerFragment()
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_container, matchContainerFragment, MatchContainerFragment::class.java.simpleName)
          .commit()
    }
  }

  private fun loadTeamsFragment(savedInstanceState: Bundle?) {
    val teamsFragment = TeamsFragment()
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_container, teamsFragment, TeamsFragment::class.java.simpleName)
          .commit()
    }
  }

  private fun loadFavoriteContainerFragment(savedInstanceState: Bundle?) {
    val favoriteContainerFragment = FavoriteContainerFragment()
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_container, favoriteContainerFragment, FavoriteContainerFragment::class.java.simpleName)
          .commit()
    }
  }

  private fun loadMatchesFragment(savedInstanceState: Bundle?, isPrevMatchMode: Boolean) {
    val matchesFragment = MatchesFragment().apply {
      arguments = Bundle().apply {
        putBoolean(MatchesFragment.KEY_IS_PREV_MATCH_MODE, isPrevMatchMode)
      }
    }

    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_container, matchesFragment, MatchesFragment::class.java.simpleName)
          .commit()
    }
  }

  private fun loadFavoritesFragment(savedInstanceState: Bundle?) {
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.main_container, FavoriteMatchesFragment(), FavoriteMatchesFragment::class.java.simpleName)
          .commit()
    }
  }
}
