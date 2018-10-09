package com.dansuse.playwithkotlin

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.dansuse.playwithkotlin.view.activity.MainActivity
import com.dansuse.playwithkotlin.view.fragment.MatchesFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRecyclerViewBehaviour() {
        val matchesFragment:MatchesFragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
        IdlingRegistry.getInstance().register(matchesFragment.getIdlingResourceInTest())

        onView(withId(R.id.list_event))
                .check(matches(isDisplayed()))
        onView(withId(R.id.list_event)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(withId(R.id.list_event)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
    }
}