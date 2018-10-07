package com.dansuse.playwithkotlin

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.dansuse.playwithkotlin.view.activity.HomeActivity
import com.dansuse.playwithkotlin.view.fragment.TeamsFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest{
    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testRecyclerViewBehaviour() {
        val teamsFragment:TeamsFragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as TeamsFragment
        IdlingRegistry.getInstance().register(teamsFragment.getIdlingResourceInTest())

        onView(withId(R.id.list_team))
                .check(matches(isDisplayed()))
        onView(withId(R.id.list_team)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(withId(R.id.list_team)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
    }
}