package com.dansuse.playwithkotlin.service

import com.dansuse.playwithkotlin.BuildConfig
import com.dansuse.playwithkotlin.model.*
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class TheSportDBApiServiceShould{
    private val okHttpClient = OkHttpClient()

    private val webServer = MockWebServer()

    private lateinit var service: TheSportDBApiService

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
                .baseUrl(webServer.url(""))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create(TheSportDBApiService::class.java)
    }

    @Test
    fun return_15_events_on_200_http_response(){
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(getJson("json/events_response.json"))
        webServer.enqueue(mockResponse)
        val expectedEvents = listOf(
                Event(
                        id = "576548",
                        title = "Fulham vs Arsenal",
                        date = "07/10/18",
                        time = "11:00:00+00:00",
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
                        awayBadge = null,
                        homeBadge = null
                ),
                Event(
                        id = "576543",
                        date = "07/10/18",
                        time = "13:15:00+00:00",
                        title = "Southampton vs Chelsea",
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
        var actualEvents: List<Event>? = null
        service.get15EventsByLeagueId(
                TheSportDBApiService.MODE_PAST_15_EVENTS, 4328
        ).subscribe{
            actualEvents = it.events
        }
        Assert.assertEquals(expectedEvents, actualEvents)
    }

    @Test
    fun return_team_detail_on_200_http_response(){
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(getJson("json/team_response.json"))
        webServer.enqueue(mockResponse)

        val expectedTeam = Team(
                teamId = "133600",
                teamName = "Fulham",
                teamBadge = "https://www.thesportsdb.com/images/media/team/badge/xwwvyt1448811086.png",
                teamFormedYear = "1879",
                teamDescription = "Fulham Football Club is a professional association football club based in Fulham, Greater London, England. Founded in 1879, they play in the Championship, having been relegated from the Premier League in 2013–14 after 13 consecutive seasons in the top-flight. They are the oldest-established football team from London to have played in the Premier League. After selling their top striker, Ross McCormack, and allowing Moussa Dembele to go on a free to Celtic, all hope of promotion back to the heights of the premier league were lost. Recently, they lost 2-1 to their rivals QPR (Queens Park Rangers) at home, the first time in 36 years that had happened. \\r\\n\\r\\nThe club has spent 25 seasons in English football's top division, the majority of these in two spells during the 1960s and 2000s. The latter spell was associated with former chairman Mohamed Al-Fayed, after the club had climbed up from the fourth tier in the 1990s. Fulham have never won a major honour, although they have reached two major finals: in 1975, as a Second Division team, they contested the FA Cup Final for the only time in their history, losing 2–0 to West Ham United, and in 2010 they reached the final of the UEFA Europa League, which they contested with Atlético Madrid in Hamburg, losing 2–1 after extra time.\\r\\n\\r\\nThe club has produced many English greats, including Johnny Haynes, George Cohen, Bobby Robson, Rodney Marsh and Alan Mullery. They play at Craven Cottage, a ground on the banks of the River Thames in Fulham which has been their home since 1896. Fulham's training ground is located near Motspur Park, where the club's Academy is also situated.",
                teamStadium = "Craven Cottage"
        )

        var actualTeam: Team? = null
        service.getTeamDetail(
                133600
        ).subscribe{teamResponse ->
            actualTeam = teamResponse.teams[0]
        }
//        expectedTeam.teamDescription = expectedTeam.teamDescription?.replace("\\n", "")
//        expectedTeam.teamDescription = expectedTeam.teamDescription?.replace("\\r", "")
//        actualTeam?.teamDescription = actualTeam?.teamDescription?.replace("\\n", "")
//        actualTeam?.teamDescription = actualTeam?.teamDescription?.replace("\\r", "")
        Assert.assertEquals(expectedTeam.teamId, actualTeam?.teamId)
        Assert.assertEquals(expectedTeam.teamName, actualTeam?.teamName)
        Assert.assertEquals(expectedTeam.teamBadge, actualTeam?.teamBadge)
        Assert.assertEquals(expectedTeam.teamFormedYear, actualTeam?.teamFormedYear)
        Assert.assertEquals(expectedTeam.teamStadium, actualTeam?.teamStadium)
    }

    @Test
    fun return_event_detail_on_200_http_response(){
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(getJson("json/event_detail_response.json"))
        webServer.enqueue(mockResponse)

        val expectedEventDetail = Event(
                id = "576548",
                title = "Fulham vs Arsenal",
                time = "11:00:00+00:00",
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
                awayBadge = null,
                homeBadge = null
        )

        var actualEventDetail: Event? = null
        service.getEventDetail(
                576548
        ).subscribe{eventDetailResponse ->
            actualEventDetail = eventDetailResponse.events[0]
        }
        Assert.assertEquals(expectedEventDetail, actualEventDetail)
    }

    @Test
    fun return_leagues_on_200_http_response(){
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(getJson("json/leagues_response.json"))
        webServer.enqueue(mockResponse)

        val expectedLeagues = listOf(
            League(
                id = "4328",
                leagueName = "English Premier League"
            ),
            League(
                id = "4329",
                leagueName = "English League Championship"
            )
        )

        var actualLeagues: List<League>? = null
        service.getAllLeagues()
                .subscribe{leaguesResponse ->
            actualLeagues = leaguesResponse.leagues
        }
        Assert.assertEquals(expectedLeagues, actualLeagues)
    }

    @Test()
    fun throw_http_exception_when_get_events_on_non_200_http_response(){
        val testObserver = TestObserver<EventResponse>()
        val leagueId = 4328
        val path = "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/" + TheSportDBApiService.MODE_PAST_15_EVENTS + "?id=" + leagueId.toString()
        val mockResponse = MockResponse()
                .setResponseCode(403)
        webServer.enqueue(mockResponse)

        service.get15EventsByLeagueId(
                TheSportDBApiService.MODE_PAST_15_EVENTS, leagueId)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoValues()
        assertEquals(1, testObserver.errorCount())

        val request = webServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Test()
    fun throw_http_exception_when_get_leagues_on_non_200_http_response(){
        val testObserver = TestObserver<LeagueResponse>()
        val path = "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/all_leagues.php"
        val mockResponse = MockResponse()
                .setResponseCode(403)
        webServer.enqueue(mockResponse)

        service.getAllLeagues()
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoValues()
        assertEquals(1, testObserver.errorCount())

        val request = webServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Test()
    fun throw_http_exception_when_get_event_detail_on_non_200_http_response(){
        val testObserver = TestObserver<EventResponse>()
        val eventId = 576548
        val path = "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/lookupevent.php?id=" + eventId.toString()
        val mockResponse = MockResponse()
                .setResponseCode(403)
        webServer.enqueue(mockResponse)

        service.getEventDetail(eventId)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoValues()
        assertEquals(1, testObserver.errorCount())

        val request = webServer.takeRequest()
        assertEquals(path, request.path)
    }

    @Test()
    fun throw_http_exception_when_get_team_detail_on_non_200_http_response(){
        val testObserver = TestObserver<TeamResponse>()
        val teamId = 133600
        val path = "/api/v1/json/" + BuildConfig.TSDB_API_KEY + "/lookupteam.php?id=" + teamId.toString()
        val mockResponse = MockResponse()
                .setResponseCode(403)
        webServer.enqueue(mockResponse)

        service.getTeamDetail(teamId)
                .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoValues()
        assertEquals(1, testObserver.errorCount())

        val request = webServer.takeRequest()
        assertEquals(path, request.path)
    }

    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    fun getJson(path : String) : String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}