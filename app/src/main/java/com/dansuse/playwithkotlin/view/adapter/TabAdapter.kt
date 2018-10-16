package com.dansuse.playwithkotlin.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class TabAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm){
    private val fragmentList:MutableList<Fragment> = mutableListOf()
    private val fragmentTitleList:MutableList<String> = mutableListOf()

    override fun getItem(index: Int): Fragment {
        return fragmentList[index]
    }

    fun addFragment(fragment: Fragment, title:String){
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}