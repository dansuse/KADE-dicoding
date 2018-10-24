package com.dansuse.playwithkotlin.view.matches

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

import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class MatchContainerFragment : Fragment(), AnkoComponent<Context> {

  private lateinit var matchTabAdapter: MatchTabAdapter
  private lateinit var tabLayout: TabLayout
  private lateinit var viewPager: ViewPager

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    matchTabAdapter = MatchTabAdapter(childFragmentManager)
    viewPager.adapter = matchTabAdapter

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
        id = R.id.tab_layout_matches
        minimumHeight = dimenAttr(R.attr.actionBarSize)
        setTabTextColors(Color.LTGRAY, Color.WHITE)
        setSelectedTabIndicatorColor(Color.WHITE)
        setBackgroundResource(R.color.colorPrimary)
        tabMode = TabLayout.MODE_FIXED

      }.lparams(width = matchParent, height = wrapContent)
      viewPager = viewPager {
        id = R.id.view_pager_matches
      }.lparams(width = matchParent, height = matchParent)
    }
  }

  fun getFragment(index:Int):Fragment{
    return matchTabAdapter.getItem(index)
  }

  fun getViewPager():ViewPager{
    return viewPager
  }
}