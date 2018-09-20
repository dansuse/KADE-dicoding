package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.MainView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainPresenter (
    private val view: MainView,
    private val theSportDBApiService: TheSportDBApiService){

  fun getLeagueList(){
    leagueDisposable = theSportDBApiService.getAllLeagues()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          leagueResponse -> view.hideLoading()
          view.showLeagueList(leagueResponse.leagues)
        },
        {error ->
          view.showErrorMessage(error.message ?: "Terjadi kesalahan saat mencoba mengambil data")}
        )
  }

  var eventDisposable: Disposable?=null
  var leagueDisposable: Disposable?=null

  fun get15EventsByLeagueId(leagueId:String, isPrevMatchMode:Boolean){
    if(leagueId != ""){
      view.showLoading()
      eventDisposable = theSportDBApiService.get15EventsByLeagueId(
          if(isPrevMatchMode) TheSportDBApiService.MODE_PAST_15_EVENTS
          else TheSportDBApiService.MODE_NEXT_15_EVENTS,
          leagueId.toInt())
          .map{ eventResponse -> eventResponse.events }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext{
            events -> view.hideLoading()
            view.showEventList(events)
          }
          .doOnError{
            error -> view.showErrorMessage(error.message?:"Terjadi Error")
          }
          .observeOn(Schedulers.io())
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
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              {result -> view.hideLoading()
                view.showEventList(result)
              },
              {error -> view.hideLoading()
                view.showErrorMessage(error.localizedMessage)}
          )
    }
  }

  fun dispose(){
    eventDisposable?.dispose()
    leagueDisposable?.dispose()
  }
}
