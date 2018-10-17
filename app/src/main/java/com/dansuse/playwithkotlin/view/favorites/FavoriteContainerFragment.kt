package com.dansuse.playwithkotlin.view.favorites

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.view.adapter.FavoriteTabAdapter

import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class FavoriteContainerFragment : Fragment(), AnkoComponent<Context> {

  private lateinit var favoriteTabAdapter: FavoriteTabAdapter
  private lateinit var tabLayout: TabLayout
  private lateinit var viewPager: ViewPager

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    favoriteTabAdapter = FavoriteTabAdapter(childFragmentManager)
    viewPager.adapter = favoriteTabAdapter

    tabLayout.setupWithViewPager(viewPager)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui) {
    linearLayout {
      orientation = LinearLayout.VERTICAL
      lparams(width = matchParent, height = matchParent)
      tabLayout = tabLayout {
        id = R.id.tab_layout_favorites
        minimumHeight = dimenAttr(R.attr.actionBarSize)
        setSelectedTabIndicatorColor(Color.WHITE)
        setBackgroundResource(R.color.colorPrimary)
        tabMode = TabLayout.MODE_FIXED

      }.lparams(width = matchParent, height = wrapContent)
      viewPager = viewPager {
        id = R.id.view_pager_favorites
      }.lparams(width = matchParent, height = matchParent)
    }
  }
}