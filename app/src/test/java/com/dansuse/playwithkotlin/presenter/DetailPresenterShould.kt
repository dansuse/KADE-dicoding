package com.dansuse.playwithkotlin.presenter

import android.accounts.NetworkErrorException
import com.dansuse.playwithkotlin.model.*
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.DetailView
import com.dansuse.playwithkotlin.view.MainView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class DetailPresenterShould{

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

    private val homeTeam : Team = Team(
            teamId = "133600",
            teamName = "Fulham",
            teamBadge = "https://www.thesportsdb.com/images/media/team/badge/xwwvyt1448811086.png"
    )

    private val awayTeam : Team = Team(
            teamId = "133604",
            teamName = "Arsenal",
            teamBadge = "https://www.thesportsdb.com/images/media/team/badge/vrtrtp1448813175.png"
    )

    //ToDo belum di test apabila terjadi error untuk stream

    @Mock
    private
    lateinit var view: DetailView

    @Mock
    private
    lateinit var theSportDBApiService: TheSportDBApiService

    lateinit var detailPresenter: DetailPresenter
    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        detailPresenter = DetailPresenter(view, theSportDBApiService, testScheduler, testScheduler)
    }

    @Test
    fun send_result_when_get_event_detail_by_id_success() {
        //val eventId = "432867"

        val eventResponse = EventResponse(listOf(event))
        val homeTeamResponse = TeamResponse(listOf(homeTeam))
        val awayTeamResponse = TeamResponse(listOf(awayTeam))

        `when`(theSportDBApiService.getEventDetail(event.id!!.toInt())).thenReturn(Observable.just(eventResponse))
        `when`(theSportDBApiService.getTeamDetail(event.homeTeamId.toInt()))
                .thenReturn(Single.just(homeTeamResponse))
        `when`(theSportDBApiService.getTeamDetail(event.awayTeamId.toInt()))
                .thenReturn(Single.just(awayTeamResponse))
        detailPresenter.getEventDetailById(event.id!!)
        testScheduler.triggerActions()
        verify(view).showLoading()
        verify(view, times(2)).hideLoading()
        verify(view, times(2)).showEventDetail(eventResponse.events[0])
    }

    @Test
    fun send_error_when_get_event_detail_failed(){
        val errorMessage = "Terjadi network error"
        `when`(theSportDBApiService.getEventDetail(event.id!!.toInt())).thenReturn(
                Observable.error(Throwable(errorMessage))
        )

        detailPresenter.getEventDetailById(event.id!!)
        testScheduler.triggerActions()
        verify(view).showLoading()
        verify(view).hideLoading()
        verify(view).showErrorMessage(errorMessage)
    }

    @Test
    fun send_error_when_get_team_detail_failed(){
        val errorMessage = "Terjadi network error"
        val eventResponse = EventResponse(listOf(event))
        val homeTeamResponse = TeamResponse(listOf(homeTeam))
        `when`(theSportDBApiService.getEventDetail(event.id!!.toInt())).thenReturn(Observable.just(eventResponse))
        `when`(theSportDBApiService.getTeamDetail(event.homeTeamId.toInt()))
                .thenReturn(Single.just(homeTeamResponse))
        `when`(theSportDBApiService.getTeamDetail(event.awayTeamId.toInt()))
                .thenReturn(Single.error(Throwable(errorMessage)))

        detailPresenter.getEventDetailById(event.id!!)
        testScheduler.triggerActions()
        verify(view).showLoading()
        verify(view).showEventDetail(event)
        verify(view, times(2)).hideLoading()
        verify(view).showErrorMessage(errorMessage)
    }
}