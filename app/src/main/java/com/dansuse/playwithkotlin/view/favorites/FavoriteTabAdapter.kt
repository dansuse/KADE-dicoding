package com.dansuse.playwithkotlin.view.favorites

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dansuse.playwithkotlin.view.favorites.favoritematches.FavoriteMatchesFragment
import com.dansuse.playwithkotlin.view.favorites.favoriteteams.FavoriteTeamsFragment

class FavoriteTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

  override fun getItem(index: Int): Fragment {
    if(index == 0){
      return FavoriteMatchesFragment()
    }else{
      return FavoriteTeamsFragment()
    }
  }

  override fun getCount(): Int {
    return 2
  }

  override fun getPageTitle(position: Int): CharSequence? {
    if (position == 0) {
      return "MATCHES"
    }
    return "TEAMS"
  }
}