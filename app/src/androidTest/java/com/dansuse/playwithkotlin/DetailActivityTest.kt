package com.dansuse.playwithkotlin

import android.content.Intent
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.presenter.DetailPresenter
import com.dansuse.playwithkotlin.view.activity.DetailActivity
import com.dansuse.playwithkotlin.view.fragment.MatchesFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import kotlin.concurrent.timer
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import org.hamcrest.Matchers

@RunWith(AndroidJUnit4::class)
class DetailActivityTest{
    private val detailPresenter = Mockito.mock(DetailPresenter::class.java)

    private val eventId = "576548"
    private val event:Event = Event(
            id = "576548",
            date = "07/10/18",
            homeScore = "1",
            awayScore = "5",
            homeTeamName = "Fulham",
            awayTeamName = "Arsenal",
            homeFormation = "",
            awayFormation = "",
            homeGoalDetails = "44':Andre Schuerrle;",
            awayGoalDetails = "29':Alexandre Lacazette;49':Alexandre Lacazette;68':Aaron Ramsey;79':Pierre-Emerick Aubameyang;90':Pierre-Emerick Aubameyang;",
            homeShots = null,
            awayShots = null,
            homeGoalKeeper = "Marcus Bettinelli; ",
            awayGoalKeeper = "Bernd Leno; ",
            homeLineupDefense = "Denis Odoi; Tim Ream; Alfie Mawson; Maxime Le Marchand; ",
            homeLineupMidfield = "Tom Cairney; Jean Michael Seri; Andre Schuerrle; Luciano Dario Vietto; Ryan Sessegnon; ",
            homeLineupForward = "Aleksandar Mitrovic; ",
            homeLineupSubstitutes = "Sergio Rico; Alfie Mawson; Kevin McDonald; Stefan Johansen; Steven Sessegnon; Floyd Ayite; Aboubakar Kamara; ",
            homeTeamId = "133600",
            awayLineupDefense = "Hector Bellerin; Shkodran Mustafi; Sokratis Papastathopoulos; Nacho Monreal; ",
            awayLineupMidfield = "Lucas Torreira; Granit Xhaka; Mesut Oezil; Aaron Ramsey; Pierre-Emerick Aubameyang; ",
            awayLineupForward = "Alexandre Lacazette; ",
            awayLineupSubstitutes = "Emiliano Martinez; Sokratis Papastathopoulos; Stephan Lichtsteiner; Sead Kolasinac; Aaron Ramsey; Matteo Guendouzi; Pierre-Emerick Aubameyang; ",
            awayTeamId = "133604",
            awayBadge = "https://www.thesportsdb.com/images/media/team/badge/vrtrtp1448813175.png",
            homeBadge = "https://www.thesportsdb.com/images/media/team/badge/xwwvyt1448811086.png"
    )

    @Rule
    @JvmField var detailActivityRule =
            object : ActivityTestRule<DetailActivity>(DetailActivity::class.java){
                override fun getActivityIntent(): Intent {
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, DetailActivity::class.java).apply {
                        putExtra("event", eventId)
                    }
                }

                override fun beforeActivityLaunched() {
                    DetailActivity.presenter = detailPresenter
                }
            }

    @Test
    fun subscribe_to_presenter_and_load_event_detail(){
        verify(detailPresenter, times(1)).getEventDetailById(eventId)
    }

    @Test
    fun ui_show_data_as_presenter_say(){
        detailActivityRule.activity.runOnUiThread {
            detailActivityRule.activity.hideLoading()
            detailActivityRule.activity.showEventDetail(event)
        }
        onView(withId(R.id.detail_activity_scroll_view)).check(matches(isDisplayed()))
        onView(withText("Fulham")).check(matches(isDisplayed()))
        onView(withText("Arsenal")).check(matches(isDisplayed()))
        onView(Matchers.allOf(withId(R.id.home_goals), withText(Matchers.containsString("Andre Schuerrle")))).check(matches(isDisplayed()))
        onView(Matchers.allOf(withId(R.id.away_goals), withText(Matchers.containsString("Alexandre Lacazette")))).check(matches(isDisplayed()))
    }

    @Test
    fun click_favorite_show_snackbar_is_saved_click_again_show_snackbar_unsaved(){
        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")
        detailActivityRule.activity.runOnUiThread {
            detailActivityRule.activity.hideLoading()
            detailActivityRule.activity.showEventDetail(event)
        }
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText(R.string.event_added_to_favorites))
                .check(matches(isDisplayed()))
        //SystemClock.sleep(5000)
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText(R.string.event_removed_from_favorites))
                .check(matches(isDisplayed()))
        //SystemClock.sleep(5000)
    }
}