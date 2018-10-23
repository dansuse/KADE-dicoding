package com.dansuse.playwithkotlin.view.teamdetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.dansuse.playwithkotlin.view.teamdetail.players.PlayersFragment
import com.dansuse.playwithkotlin.view.teamdetail.overview.TeamOverviewFragment

class TeamDetailTabAdapter(fm: FragmentManager, private val teamId:String) : FragmentPagerAdapter(fm) {
//    private val fragmentList:MutableList<Fragment> = mutableListOf()
//    private val fragmentTitleList:MutableList<String> = mutableListOf()


  override fun getItem(index: Int): Fragment {
    Log.d("tes123", "terpanggil $index")
    if(index == 0){
      val fragment = TeamOverviewFragment()
      fragment.arguments = Bundle().apply {
        putString(TeamOverviewFragment.EXTRA_TEAM_ID, teamId)
      }
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