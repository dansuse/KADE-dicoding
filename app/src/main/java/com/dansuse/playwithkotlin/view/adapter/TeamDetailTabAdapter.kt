package com.dansuse.playwithkotlin.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dansuse.playwithkotlin.presenter.teamdetail.TeamDetailPresenter
import com.dansuse.playwithkotlin.view.matches.MatchesFragment
import com.dansuse.playwithkotlin.view.teamdetail.PlayersFragment
import com.dansuse.playwithkotlin.view.teamdetail.TeamOverviewFragment

class TeamDetailTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//    private val fragmentList:MutableList<Fragment> = mutableListOf()
//    private val fragmentTitleList:MutableList<String> = mutableListOf()

  var teamId:String? = null

  override fun getItem(index: Int): Fragment {
    if(index == 0){
      val fragment = TeamOverviewFragment()
//      fragment.arguments = Bundle().apply {
//        putString(TeamOverviewFragment.EXTRA_TEAM_DESCRIPTION, "asd")
//      }
      return fragment
    }else{
      val fragment = PlayersFragment().apply {
        arguments = Bundle().apply {
          putString(PlayersFragment.EXTRA_TEAM_ID, teamId)
        }
      }
      return fragment
//      val fragment = TeamOverviewFragment()
//      fragment.arguments = Bundle().apply {
//        putString(TeamOverviewFragment.EXTRA_TEAM_DESCRIPTION, "asd")
//      }
//      return fragment
    }
  }

  override fun getCount(): Int {
    return 2
  }

  override fun getPageTitle(position: Int): CharSequence? {
    if (position == 0) {
      return "OVERVIEW"
    }
    return "PLAYERS"
  }
}