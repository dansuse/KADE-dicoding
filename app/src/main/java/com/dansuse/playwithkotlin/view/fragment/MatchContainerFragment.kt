package com.dansuse.playwithkotlin.view.fragment

import android.content.Context
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.LinearLayout
import com.dansuse.playwithkotlin.R
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class MatchContainerFragment : Fragment(), AnkoComponent<Context>{

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            orientation = LinearLayout.VERTICAL
            lparams(width = matchParent, height = matchParent)
            tabLayout {
                id = R.id.tab_layout
                minimumHeight = dimenAttr(R.attr.actionBarSize)
                setSelectedTabIndicatorColor(Color.WHITE)
                setBackgroundResource(R.color.colorPrimary)
                tabMode = TabLayout.MODE_FIXED

            }.lparams(width = matchParent, height = wrapContent)
            viewPager {

            }.lparams(width = matchParent, height = matchParent)
        }
    }
}