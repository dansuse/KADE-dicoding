package com.dansuse.playwithkotlin.presenter

import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.DetailView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

open class DetailPresenter(
    private val view: DetailView,
    private val theSportDBApiService: TheSportDBApiService,
    private val processScheduler: Scheduler,
    private val androidScheduler: Scheduler
) {

  var disposable: Disposable? = null

  open fun getEventDetailById(eventId: String) {
    //EspressoIdlingResource.mCountingIdlingResource.increment()

    view.showLoading()
    disposable = theSportDBApiService.getEventDetail(eventId.toInt())
        .map { eventResponse -> eventResponse.events[0] }
        .observeOn(androidScheduler)
        .doOnNext { event ->
          view.hideLoading()
          view.showEventDetail(event)
        }
        .observeOn(processScheduler)
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
        }.subscribeOn(processScheduler)
        .observeOn(androidScheduler)
        .subscribe(
            { result ->
              view.hideLoading()
              view.showEventDetail(result)

              //EspressoIdlingResource.mCountingIdlingResource.decrement()
            },
            { error ->
              view.hideLoading()
              view.showErrorMessage(error.localizedMessage)

              //EspressoIdlingResource.mCountingIdlingResource.decrement()
            }
        )

  }

  fun dispose() {
    disposable?.dispose()
  }
}