package com.dansuse.playwithkotlin

import android.support.test.espresso.idling.CountingIdlingResource

class EspressoIdlingResource{
    companion object {
        private const val RESOURCE:String = "GLOBAL"
        var mCountingIdlingResource : CountingIdlingResource =
                CountingIdlingResource(RESOURCE)
    }
}