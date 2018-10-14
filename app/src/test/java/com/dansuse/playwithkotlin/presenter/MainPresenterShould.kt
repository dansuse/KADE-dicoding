package com.dansuse.playwithkotlin.presenter

import android.accounts.NetworkErrorException
import com.dansuse.playwithkotlin.model.*
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
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
import java.util.concurrent.TimeUnit

class MainPresenterShould {

    private val leagueId = "4328"

    @Mock
    private
    lateinit var view: MainView

    @Mock
    private
    lateinit var theSportDBApiService: TheSportDBApiService

//    lateinit var mockServer : MockWebServer
    lateinit var mainPresenter: MainPresenter
    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        //RxAndroidPlugins.setInitMainThreadSchedulerHandler({ Schedulers.trampoline()})
        MockitoAnnotations.initMocks(this)
//        mockServer = MockWebServer()
//        mockServer.start()
//
//        // Get an okhttp client
//        val okHttpClient = OkHttpClient.Builder()
//                .build()
//
//        // Get an instance of Retrofit
//        val retrofit = Retrofit.Builder()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl("https://api.blogs.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build()
//
//        // Get an instance of blogService
//        theSportDBApiService = retrofit.create(TheSportDBApiService::class.java)
        testScheduler = TestScheduler()
        mainPresenter = MainPresenter(view, theSportDBApiService, testScheduler, testScheduler)
    }

    @After
    fun tearDown() {
//        mockServer.shutdown()
    }

    @Test
    fun send_result_when_get_leagues_success() {
//        val testObserver = TestObserver<LeagueResponse>()
//
//        val path = "/blogs"
//
//        // Mock a response with status 200 and sample JSON output
//        val mockResponse = MockResponse()
//                .setResponseCode(200)
//                .setBody(getJson("json/blog/blogs.json"))
//        // Enqueue request
//        mockServer.enqueue(mockResponse)
//
//        // Call the API
//        theSportDBApiService.getAllLeagues().subscribe(testObserver)
//        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
//
//        // No errors
//        testObserver.assertNoErrors()
//        // One list emitted
//        testObserver.assertValueCount(1)
//
//        // Get the request that was just made
//        val request = mockServer.takeRequest()
//        // Make sure we made the request to the required path
//        assertEquals(path, request.path)

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
        mainPresenter.getLeagueList()
        testScheduler.triggerActions()
        verify(view).hideLoading()
        verify(view).showLeagueList(leagueResponse.leagues)
    }

    @Test
    fun send_error_when_get_leagues_failed(){
        val exception = NetworkErrorException()
        `when`(theSportDBApiService.getAllLeagues()).thenReturn(
                Observable.error(exception))
        mainPresenter.getLeagueList()
        testScheduler.triggerActions()
        verify(view).showErrorMessage(exception.message ?: "Terjadi kesalahan saat mencoba mengambil data")
    }

    @Test
    fun send_result_when_get_15_events_by_league_id_success() {

        val isPrevMatchMode = true
        val mode = if(isPrevMatchMode)TheSportDBApiService.MODE_PAST_15_EVENTS else
        TheSportDBApiService.MODE_NEXT_15_EVENTS

        val eventResponse:EventResponse = EventResponse(ArrayList<Event>())
        val teamResponse:TeamResponse = TeamResponse(ArrayList<Team>())
        //val awayTeamResponse:TeamResponse = TeamResponse(ArrayList<Team>())

        `when`(theSportDBApiService.get15EventsByLeagueId(
                mode, leagueId.toInt())).thenReturn(Observable.just(eventResponse))
        `when`(theSportDBApiService.getTeamDetail(ArgumentMatchers.anyInt()))
                .thenReturn(Single.just(teamResponse))
        mainPresenter.get15EventsByLeagueId(leagueId, isPrevMatchMode)
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
        mainPresenter.get15EventsByLeagueId(leagueId, true)
        testScheduler.triggerActions()
        verify(view, times(2)).showErrorMessage(errorMessage)
    }
}