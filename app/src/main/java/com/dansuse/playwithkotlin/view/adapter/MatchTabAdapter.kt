package com.dansuse.playwithkotlin.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dansuse.playwithkotlin.view.matches.MatchesFragment

class MatchTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//    private val fragmentList:MutableList<Fragment> = mutableListOf()
//    private val fragmentTitleList:MutableList<String> = mutableListOf()

  override fun getItem(index: Int): Fragment {
    val fragment = MatchesFragment()
    fragment.arguments = Bundle().apply {
      putBoolean(MatchesFragment.KEY_IS_PREV_MATCH_MODE, (index == 1))
    }
    return fragment
  }

//    fun addFragment(fragment: Fragment, title:String){
//        fragmentList.add(fragment)
//        fragmentTitleList.add(title)
//    }

  override fun getCount(): Int {
    return 2
  }

  override fun getPageTitle(position: Int): CharSequence? {
    if (position == 0) {
      return "NEXT"
    }
    return "PAST"
  }
}