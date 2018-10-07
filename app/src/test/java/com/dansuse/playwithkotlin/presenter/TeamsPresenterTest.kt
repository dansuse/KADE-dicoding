package com.dansuse.playwithkotlin.presenter

import android.support.test.espresso.idling.CountingIdlingResource
import com.dansuse.playwithkotlin.model.Team
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.ApiRepository
import com.dansuse.playwithkotlin.repository.TheSportDBApi
import com.dansuse.playwithkotlin.view.TeamsView
import com.dansuse.playwithkotlin.view.TestContextProvider
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.objectMockk
import io.mockk.use
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class TeamsPresenterTest {

    @Mock
    private lateinit var view:TeamsView

    @Mock
    private lateinit var gson:Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var presenter:TeamsPresenter

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        presenter = TeamsPresenter(view, apiRepository, gson, TestContextProvider(), CountingIdlingResource(""))
    }

    @Test
    fun getTeamList() {
        val teams:MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val league = "English Premiere League"

        mockkObject(TheSportDBApi).apply {
            every { TheSportDBApi.getTeams(league) } returns ""
            `when`(gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getTeams(league)),
                    TeamResponse::class.java)
            ).thenReturn(response)

            presenter.getTeamList(league)

            verify(view).showLoading()
            verify(view).showTeamList(teams)
            verify(view).hideLoading()
        }
    }
}