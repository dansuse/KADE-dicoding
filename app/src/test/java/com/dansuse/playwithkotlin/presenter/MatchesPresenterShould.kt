package com.dansuse.playwithkotlin.presenter

import android.accounts.NetworkErrorException
import com.dansuse.playwithkotlin.model.*
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.matches.MatchesView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MatchesPresenterShould {

    private val leagueId = "4328"

    @Mock
    private
    lateinit var view: MatchesView

    @Mock
    private
    lateinit var theSportDBApiService: TheSportDBApiService

    lateinit var matchesPresenter: MatchesPresenter
    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        testScheduler = TestScheduler()
        matchesPresenter = MatchesPresenter(view, theSportDBApiService, testScheduler, testScheduler)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun send_result_when_get_leagues_success() {

        val leagueResponse = LeagueResponse(listOf(
            League(
                    id = "4328",
                    leagueName = "English Premier League"
            ),
            League(
                    id = "4329",
                    leagueName = "English League Championship"
            )
        ))
        `when`(theSportDBApiService.getAllLeagues()).thenReturn(Observable.just(leagueResponse))
        matchesPresenter.getLeagueList()
        testScheduler.triggerActions()
        verify(view).hideLoading()
        verify(view).showLeagueList(leagueResponse.leagues)
    }

    @Test
    fun send_error_when_get_leagues_failed(){
        val exception = NetworkErrorException()
        `when`(theSportDBApiService.getAllLeagues()).thenReturn(
                Observable.error(exception))
        matchesPresenter.getLeagueList()
        testScheduler.triggerActions()
        verify(view).showErrorMessage(exception.message ?: "Terjadi kesalahan saat mencoba mengambil data")
    }

    @Test
    fun send_result_when_get_15_events_by_league_id_success() {

        val isPrevMatchMode = true
        val mode = if(isPrevMatchMode)TheSportDBApiService.MODE_PAST_15_EVENTS else
        TheSportDBApiService.MODE_NEXT_15_EVENTS

        val eventResponse:EventResponse = EventResponse(ArrayList<Event>(), ArrayList<Event>())
        val teamResponse:TeamResponse = TeamResponse(ArrayList<Team>())

        `when`(theSportDBApiService.get15EventsByLeagueId(
                mode, leagueId.toInt())).thenReturn(Observable.just(eventResponse))
        `when`(theSportDBApiService.getTeamDetail(ArgumentMatchers.anyInt()))
                .thenReturn(Single.just(teamResponse))
        matchesPresenter.get15EventsByLeagueId(leagueId, isPrevMatchMode)
        testScheduler.triggerActions()
        verify(view).showLoading()
        verify(view, times(2)).hideLoading()
        verify(view, times(2)).showEventList(eventResponse.events)

    }

    @Test
    fun send_error_when_get_15_events_by_league_id_failed(){
        val errorMessage = "Terjadi network error"
        `when`(theSportDBApiService.get15EventsByLeagueId(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(Observable.error(Throwable(message = errorMessage)))
        matchesPresenter.get15EventsByLeagueId(leagueId, true)
        testScheduler.triggerActions()
        verify(view, times(2)).showErrorMessage(errorMessage)
    }
}