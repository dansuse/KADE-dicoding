package com.dansuse.playwithkotlin.presenter

import android.support.test.espresso.idling.CountingIdlingResource
import com.dansuse.playwithkotlin.EspressoIdlingResource
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.TeamResponse
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.DetailView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

open class DetailPresenter (
    private val view: DetailView,
    private val theSportDBApiService: TheSportDBApiService
    ){

  var disposable: Disposable?=null

  open fun getEventDetailById(eventId:String){
      //EspressoIdlingResource.mCountingIdlingResource.increment()

    view.showLoading()
    disposable = theSportDBApiService.getEventDetail(eventId.toInt())
        .map{ eventResponse -> eventResponse.events[0] }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext{
          event -> view.hideLoading()
          view.showEventDetail(event)
        }
        .observeOn(Schedulers.io())
        .flatMap { event -> Single.zip(
            theSportDBApiService.getTeamDetail(event.homeTeamId.toInt()),
            theSportDBApiService.getTeamDetail(event.awayTeamId.toInt())
            , BiFunction<TeamResponse, TeamResponse, Event> { homeTeam, awayTeam ->
          event.homeBadge = homeTeam.teams[0].teamBadge
          event.awayBadge = awayTeam.teams[0].teamBadge
          return@BiFunction event
        }
        ).toObservable()
        }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {result -> view.hideLoading()
              view.showEventDetail(result)

                //EspressoIdlingResource.mCountingIdlingResource.decrement()
            },
            {error -> view.hideLoading()
              view.showErrorMessage(error.localizedMessage)

                //EspressoIdlingResource.mCountingIdlingResource.decrement()
            }
        )

  }

  fun dispose(){
    disposable?.dispose()
  }
}