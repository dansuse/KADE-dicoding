package com.dansuse.playwithkotlin

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League
import com.dansuse.playwithkotlin.presenter.MatchDetailPresenter
import com.dansuse.playwithkotlin.presenter.MatchesPresenter
import com.dansuse.playwithkotlin.view.matchdetail.MatchDetailActivity
import com.dansuse.playwithkotlin.view.activity.MainActivity
import com.dansuse.playwithkotlin.view.matches.MatchesFragment
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class MainActivityShould {

    private val mainPresenter = Mockito.mock(MatchesPresenter::class.java)
    private val detailPresenter = Mockito.mock(MatchDetailPresenter::class.java)

    private val events = listOf(
            Event(
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
            ),
            Event(
                    id = "576543",
                    date = "07/10/18",
                    homeScore = "0",
                    awayScore = "3",
                    homeTeamName = "Southampton",
                    awayTeamName = "Chelsea",
                    homeFormation = "",
                    awayFormation = "",
                    homeGoalDetails = "",
                    awayGoalDetails = "31':Eden Hazard;57':Ross Barkley;90':Alvaro Morata;",
                    homeShots = null,
                    awayShots = null,
                    homeGoalKeeper = "Alex McCarthy; ",
                    awayGoalKeeper = "Kepa Arrizabalaga; ",
                    homeLineupDefense = "Jack Stephens; Jannik Vestergaard; Wesley Hoedt; ",
                    homeLineupMidfield = "Cedric Soares; Pierre-Emile Hoejbjerg; Mario Lemina; Ryan Bertrand; Mohamed Elyounoussi; Nathan Redmond; ",
                    homeLineupForward = "Danny Ings; ",
                    homeLineupSubstitutes = "Angus Gunn; Jannik Vestergaard; Matt Targett; Steven Davis; Oriol Romeu; Shane Long; Charlie Austin; ",
                    homeTeamId = "134778",
                    awayLineupDefense = "Cesar Azpilicueta; Antonio Ruediger; David Luiz; Marcos Alonso; ",
                    awayLineupMidfield = "N'Golo Kante; Jorginho; Mateo Kovacic; ",
                    awayLineupForward = "Willian; Olivier Giroud; Eden Hazard; ",
                    awayLineupSubstitutes = "Wilfredo Caballero; Davide Zappacosta; Gary Cahill; Cesc Fabregas; Mateo Kovacic; Pedro Rodriguez; Alvaro Morata; ",
                    awayTeamId = "133610",
                    awayBadge = null,
                    homeBadge = null
            )
    )
    private val leagues = listOf(
            League(
                    id = "4328",
                    leagueName = "English Premier League"
            ),
            League(
                    id = "4329",
                    leagueName = "English League Championship"
            )
    )


    @Rule
    @JvmField var mainActivityRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java){
        override fun beforeActivityLaunched() {
            MatchesFragment.presenter = mainPresenter
        }
    }

    @Rule
    @JvmField var detailActivityRule =
            object : ActivityTestRule<MatchDetailActivity>(MatchDetailActivity::class.java, true, false){
                override fun beforeActivityLaunched() {
                    MatchDetailActivity.presenter = detailPresenter
                }
            }

    @Before
    fun setUp(){
    }

    @Test
    fun subscribe_to_main_presenter_and_load_leagues(){
        verify(mainPresenter, times(1)).getLeagueList()
    }

    @Test
    fun load_last_15_matches_when_dropdown_value_changes(){
        `when`(mainPresenter.get15EventsByLeagueId(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                .then {
                    mainActivityRule.activity.runOnUiThread {
                        val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
                        matchesFragment.showEventList(events)
                    }
                }

        mainActivityRule.activity.runOnUiThread {
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showLeagueList(leagues)
        }

        val selectionText = leagues[1].leagueName
        onView(withId(R.id.spinner_league)).check(matches(isDisplayed()))
        verify(mainPresenter, times(1)).get15EventsByLeagueId(leagues[0].id, true)
        onView(withId(R.id.list_event)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_league)).perform(click())

        onData(`is`(instanceOf(League::class.java)))
                .atPosition(1)
                .perform(click())

        onView(withId(R.id.spinner_league)).check(matches(withSpinnerText(containsString(selectionText))))

        verify(mainPresenter, times(1)).get15EventsByLeagueId(leagues[1].id, true)
    }

    @Test
    fun open_event_detail_when_click_event_list_item() {
        Intents.init()
        mainActivityRule.activity.runOnUiThread {
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showEventList(events)
        }
        onView(withId(R.id.list_event))
                .check(matches(isDisplayed()))

        events.forEach {
            onView(withText(it.homeTeamName)).check(matches(isDisplayed()))
            onView(withText(it.awayTeamName)).check(matches(isDisplayed()))
        }
        onView(withId(R.id.list_event)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withId(R.id.list_event)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        intended(allOf(hasComponent(MatchDetailActivity::class.java.name), hasExtra("event", events[1].id)))

        Intents.release()
    }

    @Test
    fun load_next_15_matches_when_click_next_match_bottom_navigation(){
        onView(withId(R.id.action_next_match)).perform(click())
        mainActivityRule.activity.runOnUiThread {
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showLeagueList(leagues)
        }
        onView(withId(R.id.spinner_league)).check(matches(isDisplayed()))
        verify(mainPresenter).get15EventsByLeagueId(ArgumentMatchers.anyString(), ArgumentMatchers.eq(false))
    }

    @Test
    fun add_event_to_favorite_or_remove_event_from_favorite_when_menu_item_favorite_click(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent:Intent = Intent(targetContext, MatchDetailActivity::class.java).apply {
            putExtra("event", events[1].id)
        }
        detailActivityRule.launchActivity(intent)

        detailActivityRule.activity.runOnUiThread {
            detailActivityRule.activity.hideLoading()
            detailActivityRule.activity.showEventDetail(events[1])
        }
        onView(withId(R.id.detail_activity_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText(R.string.event_added_to_favorites))
                .check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.action_favorites)).perform(click())
        onView((withId(R.id.list_favorite_event))).check(matches(isDisplayed()))
        onView(withText(events[1].homeTeamName)).check(matches(isDisplayed()))
        onView(withText(events[1].awayTeamName)).check(matches(isDisplayed()))

        detailActivityRule.launchActivity(intent)

        detailActivityRule.activity.runOnUiThread {
            detailActivityRule.activity.hideLoading()
            detailActivityRule.activity.showEventDetail(events[1])
        }
        onView(withId(R.id.detail_activity_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText(R.string.event_removed_from_favorites))
                .check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.swipe_refresh_favorite_event)).perform(swipeDown())
        onView((withId(R.id.list_favorite_event))).check(matches(isDisplayed()))
        onView(withText(events[1].homeTeamName)).check(doesNotExist())
        onView(withText(events[1].awayTeamName)).check(doesNotExist())

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.mCountingIdlingResource)
    }

    @Test
    fun open_detail_event_when_click_favorite_event_list_item(){

        IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent:Intent = Intent(targetContext, MatchDetailActivity::class.java).apply {
            putExtra("event", events[1].id)
        }
        detailActivityRule.launchActivity(intent)

        detailActivityRule.activity.runOnUiThread {
            detailActivityRule.activity.hideLoading()
            detailActivityRule.activity.showEventDetail(events[1])
        }
        onView(withId(R.id.detail_activity_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText(R.string.event_added_to_favorites))
                .check(matches(isDisplayed()))
        pressBack()
        Intents.init()
        onView(withId(R.id.action_favorites)).perform(click())
        onView((withId(R.id.list_favorite_event))).check(matches(isDisplayed()))
        onView(withId(R.id.list_favorite_event)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        intended(allOf(hasComponent(MatchDetailActivity::class.java.name), hasExtra("event", events[1].id)))
        Intents.release()
    }

}