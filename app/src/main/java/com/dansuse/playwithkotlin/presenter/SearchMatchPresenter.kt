package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.searchmatch.SearchMatchView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

class SearchMatchPresenter(
    private val view: SearchMatchView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
){
  private var searchResultDisposable: Disposable? = null

  fun getSearchResult(query:String){
    view.showLoading()
    searchResultDisposable = theSportDBApiService
        .searchEvent(query)
        .map { eventResponse -> eventResponse.event }
        .observeOn(androidScheduler)
        .doOnNext { events ->
          view.hideLoading()
          view.showSearchResult(events)
        }
        .doOnError { error ->
          view.showErrorMessage(error.message ?: "Terjadi Error")
        }
        .observeOn(processScheduler)
        .flatMapIterable { events -> events }
        .flatMap { event ->
          Single.zip(
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
        .subscribe (
          {
            view.hideLoading()
            view.showSearchResult(it)
          },
          {
            view.hideLoading()
            view.showErrorMessage(it.message ?: "Terjadi error saat mendapatkan data event berdasarkan query")
          }
        )
  }

  fun dispose(){
    searchResultDisposable?.dispose()
  }
}