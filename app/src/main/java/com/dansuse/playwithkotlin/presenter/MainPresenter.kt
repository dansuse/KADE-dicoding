package com.dansuse.playwithkotlin.presenter

import android.support.test.espresso.idling.CountingIdlingResource
import com.dansuse.playwithkotlin.EspressoIdlingResource
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.MainView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

open class MainPresenter (
    private val view: MainView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler){

  open fun getLeagueList(){
      EspressoIdlingResource.mCountingIdlingResource.increment()
    leagueDisposable = theSportDBApiService.getAllLeagues()
        .subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe({
          leagueResponse -> view.hideLoading()
          view.showLeagueList(leagueResponse.leagues)
            EspressoIdlingResource.mCountingIdlingResource.decrement()
        },
        {error ->
          view.showErrorMessage(error.message ?: "Terjadi kesalahan saat mencoba mengambil data")
            EspressoIdlingResource.mCountingIdlingResource.decrement()
        }
        )
  }

  var eventDisposable: Disposable?=null
  var leagueDisposable: Disposable?=null

  open fun get15EventsByLeagueId(leagueId:String, isPrevMatchMode:Boolean){
    if(leagueId != ""){
        EspressoIdlingResource.mCountingIdlingResource.increment()
      view.showLoading()
      eventDisposable = theSportDBApiService.get15EventsByLeagueId(
          if(isPrevMatchMode) TheSportDBApiService.MODE_PAST_15_EVENTS
          else TheSportDBApiService.MODE_NEXT_15_EVENTS,
          leagueId.toInt())
          .map{ eventResponse -> eventResponse.events }
          .observeOn(androidScheduler)
          .doOnNext{
            events -> view.hideLoading()
            view.showEventList(events)
          }
          .doOnError{
            error -> view.showErrorMessage(error.message?:"Terjadi Error")
          }
          .observeOn(processScheduler)
          .flatMapIterable { events -> events }
          .flatMap { event -> Single.zip(
              theSportDBApiService.getTeamDetail(event.homeTeamId.toInt()),
              theSportDBApiService.getTeamDetail(event.awayTeamId.toInt())
              , BiFunction<TeamResponse, TeamResponse, Event> { homeTeam, awayTeam ->
            event.homeBadge = homeTeam.teams[0].teamBadge
            event.awayBadge = awayTeam.teams[0].teamBadge
            return@BiFunction event
          }
          ).toObservable()
          }.toList()
          .subscribeOn(processScheduler)
          .observeOn(androidScheduler)
          .subscribe(
              {result -> view.hideLoading()
                view.showEventList(result)
                  EspressoIdlingResource.mCountingIdlingResource.decrement()
              },
              {error -> view.hideLoading()
                view.showErrorMessage(error.localizedMessage)
                  EspressoIdlingResource.mCountingIdlingResource.decrement()
              }
          )
    }
  }

  fun dispose(){
    eventDisposable?.dispose()
    leagueDisposable?.dispose()
  }
}
