package com.dansuse.playwithkotlin

import android.content.Intent
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League
import com.dansuse.playwithkotlin.presenter.DetailPresenter
import com.dansuse.playwithkotlin.presenter.MainPresenter
import com.dansuse.playwithkotlin.view.DetailView
import com.dansuse.playwithkotlin.view.MainView
import com.dansuse.playwithkotlin.view.activity.DetailActivity
import com.dansuse.playwithkotlin.view.activity.MainActivity
import com.dansuse.playwithkotlin.view.fragment.MatchesFragment
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val mainPresenter = Mockito.mock(MainPresenter::class.java)
    private val detailPresenter = Mockito.mock(DetailPresenter::class.java)

    val events = listOf(
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
                    homeScore = "1",
                    awayScore = "5",
                    homeTeamName = "HAHAHA",
                    awayTeamName = "HEHEHE",
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
    )
    val leagues = listOf(
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
//        override fun afterActivityLaunched() {
//            //super.afterActivityLaunched()
//            val matchesFragment = this.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//            MatchesFragment.presenter = presenter
//        }
        override fun beforeActivityLaunched() {
//            doAnswer{
//                this.activity.runOnUiThread {
//                    //EspressoIdlingResource.mCountingIdlingResource.increment()
//                    val matchesFragment:MatchesFragment = this.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                    matchesFragment.showLeagueList(leagues)
//                    //EspressoIdlingResource.mCountingIdlingResource.decrement()
//                }
//                return@doAnswer null
//            }.`when`(mainPresenter).getLeagueList()
//
//            doAnswer {
//                this.activity.runOnUiThread {
//                    //EspressoIdlingResource.mCountingIdlingResource.increment()
//                    val matchesFragment:MatchesFragment = this.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                    matchesFragment.showEventList(events)
//                    //EspressoIdlingResource.mCountingIdlingResource.decrement()
//                }
//            }.`when`(mainPresenter).get15EventsByLeagueId(
//                    ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())

            MatchesFragment.presenter = mainPresenter

        }
    }

    @Rule
    @JvmField var detailActivityRule =
            object : ActivityTestRule<DetailActivity>(DetailActivity::class.java, true, false){
//                override fun getActivityIntent(): Intent {
//                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
//                    return Intent(targetContext, DetailActivity::class.java).apply {
//                        putExtra("event", "576548")
//                    }
//                }

                override fun beforeActivityLaunched() {
                    DetailActivity.presenter = detailPresenter
                }
            }

    @Before
    fun setUp(){
//        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")
        //Intents.init()
    }

    @Test
    fun click_recycler_view_item_open_detail_activity() {

//        `when`(mainPresenter.getLeagueList())
//                .then {
//                    mainActivityRule.activity.runOnUiThread {
//                        EspressoIdlingResource.mCountingIdlingResource.increment()
//                        val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                        matchesFragment.showLeagueList(leagues)
//                        EspressoIdlingResource.mCountingIdlingResource.decrement()
//                    }
//                }
//
//
//
//        `when`(mainPresenter.get15EventsByLeagueId(
//                ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
//                .then {
//                    mainActivityRule.activity.runOnUiThread {
//                        EspressoIdlingResource.mCountingIdlingResource.increment()
//                        val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                        matchesFragment.showEventList(events)
//                        EspressoIdlingResource.mCountingIdlingResource.decrement()
//                    }
//                }
//
//        `when`(detailPresenter.getEventDetailById(
//                ArgumentMatchers.anyString()))
//                .then {
//                    detailActivityRule.activity.runOnUiThread {
//                        EspressoIdlingResource.mCountingIdlingResource.increment()
//                        detailActivityRule.activity.showEventDetail(events[1])
//                        EspressoIdlingResource.mCountingIdlingResource.decrement()
//                    }
//                }


//        doAnswer{
//            mainActivityRule.activity.runOnUiThread {
//                //EspressoIdlingResource.mCountingIdlingResource.increment()
//                val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                matchesFragment.showLeagueList(leagues)
//                //EspressoIdlingResource.mCountingIdlingResource.decrement()
//            }
//            return@doAnswer null
//        }.`when`(mainPresenter).getLeagueList()
//
//        doAnswer {
//            mainActivityRule.activity.runOnUiThread {
//                //EspressoIdlingResource.mCountingIdlingResource.increment()
//                val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//                matchesFragment.showEventList(events)
//                //EspressoIdlingResource.mCountingIdlingResource.decrement()
//            }
//        }.`when`(mainPresenter).get15EventsByLeagueId(
//                ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())


        //val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
        //IdlingRegistry.getInstance().register(matchesFragment.getIdlingResourceInTest())
        //IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)

        Intents.init()
        mainActivityRule.activity.runOnUiThread {
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showEventList(events)
        }
        onView(withId(R.id.list_event))
                .check(matches(isDisplayed()))

//        verify(mainPresenter, times(1)).get15EventsByLeagueId(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())
        events.forEach {
            onView(withText(it.homeTeamName)).check(matches(isDisplayed()))
            onView(withText(it.awayTeamName)).check(matches(isDisplayed()))
        }
        onView(withId(R.id.list_event)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withId(R.id.list_event)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        intended(allOf(hasComponent(DetailActivity::class.java.name), hasExtra("event", events[1].id)))

        Intents.release()
//        detailActivityRule.activity.runOnUiThread {
//            EspressoIdlingResource.mCountingIdlingResource.increment()
//            detailActivityRule.activity.showEventDetail(events[1])
//            EspressoIdlingResource.mCountingIdlingResource.decrement()
//        }
//
//        //detailActivityRule.
//        //IdlingRegistry.getInstance().register(detailActivityRule.activity.getIdlingResourceInTest())
//        onView(withId(R.id.add_to_favorite))
//                .check(matches(isDisplayed()))
//        onView(withId(R.id.add_to_favorite)).perform(click())
//        onView(withText("Added to favorite"))
//                .check(matches(isDisplayed()))
//        onView(withText("Event is still loading"))
//                .check(matches(isDisplayed()))
//        pressBack()
    }

    @Test
    fun click_next_match_load_next_15_matches(){
        onView(withId(R.id.action_next_match)).perform(click())
        mainActivityRule.activity.runOnUiThread {
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showLeagueList(leagues)
        }
        onView(withId(R.id.spinner_league)).check(matches(isDisplayed()))
        verify(mainPresenter).get15EventsByLeagueId(ArgumentMatchers.anyString(), ArgumentMatchers.eq(false))
    }

    @Test
    fun subscribe_to_presenter_and_load_league(){
        verify(mainPresenter, times(1)).getLeagueList()
    }

    @Test
    fun select_dropdown_and_load_last_15_matches(){
        `when`(mainPresenter.get15EventsByLeagueId(
            ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
            .then {
                mainActivityRule.activity.runOnUiThread {
                    //EspressoIdlingResource.mCountingIdlingResource.increment()
                    val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
                    matchesFragment.showEventList(events)
                    //EspressoIdlingResource.mCountingIdlingResource.decrement()
                }
            }

        mainActivityRule.activity.runOnUiThread {
            //EspressoIdlingResource.mCountingIdlingResource.increment()
            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
            matchesFragment.showLeagueList(leagues)
            //EspressoIdlingResource.mCountingIdlingResource.decrement()
        }

        val selectionText = leagues[1].leagueName
        onView(withId(R.id.spinner_league)).check(matches(isDisplayed()))
        verify(mainPresenter, times(1)).get15EventsByLeagueId(leagues[0].id, true)
        onView(withId(R.id.list_event)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_league)).perform(click())
        //`is`(instanceOf(League::class.java))
        //onData(allOf(`is`(instanceOf(League::class.java)), `is`(leagues[1].toString())))
        onData(`is`(instanceOf(League::class.java)))
                .atPosition(1)
                .perform(click())
        //SystemClock.sleep(5000)
        onView(withId(R.id.spinner_league)).check(matches(withSpinnerText(containsString(selectionText))))

        verify(mainPresenter, times(1)).get15EventsByLeagueId(leagues[1].id, true)
//        onData(allOf(is(instanceOf(String::class.java), is(selectionText))))
//            .
        //verify(mainPresenter).get15EventsByLeagueId(ArgumentMatchers.anyString(), ArgumentMatchers.booleanThat { false })
    }

    @Test
    fun favorite_match_check_if_it_show_in_favorite_list(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")
//        mainActivityRule.activity.runOnUiThread {
//            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//            matchesFragment.showLeagueList(leagues)
//        }
//        mainActivityRule.activity.runOnUiThread {
//            val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//            matchesFragment.showEventList(events)
//        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent:Intent = Intent(targetContext, DetailActivity::class.java).apply {
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
        //SystemClock.sleep(5000)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.mCountingIdlingResource)
    }

    @Test
    fun favorite_item_click_open_detail_activity(){

        IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
        InstrumentationRegistry.getTargetContext().deleteDatabase("FavoriteEvent.db")

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent:Intent = Intent(targetContext, DetailActivity::class.java).apply {
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
        intended(allOf(hasComponent(DetailActivity::class.java.name), hasExtra("event", events[1].id)))
        Intents.release()
    }

//    @Test
//    fun testRecyclerViewBehaviour() {
//        //val matchesFragment:MatchesFragment = mainActivityRule.activity.supportFragmentManager.findFragmentById(R.id.main_container) as MatchesFragment
//        //IdlingRegistry.getInstance().register(matchesFragment.getIdlingResourceInTest())
//        IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
//
//        onView(withId(R.id.list_event))
//                .check(matches(isDisplayed()))
//        onView(withId(R.id.list_event)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
//        onView(withId(R.id.list_event)).perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
//
//        //detailActivityRule.
//        //IdlingRegistry.getInstance().register(detailActivityRule.activity.getIdlingResourceInTest())
//        onView(withId(R.id.add_to_favorite))
//                .check(matches(isDisplayed()))
//        onView(withId(R.id.add_to_favorite)).perform(click())
////        onView(withText("Added to favorite"))
////                .check(matches(isDisplayed()))
////        onView(withText("Event is still loading"))
////                .check(matches(isDisplayed()))
//        pressBack()
//    }

}