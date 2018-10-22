package com.dansuse.playwithkotlin.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    @SerializedName("idEvent")
    var id: String? = null,

    @SerializedName("strEvent")
    var title: String? = null,

    @SerializedName("strDate")
    var date: String? = null,

    @SerializedName("strTime")
    var time: String? = null,

    @SerializedName("intHomeScore")
    var homeScore: String? = null,
    @SerializedName("intAwayScore")
    var awayScore: String? = null,

    @SerializedName("strHomeTeam")
    var homeTeamName: String? = null,
    @SerializedName("strAwayTeam")
    var awayTeamName: String? = null,

    @SerializedName("strHomeFormation")
    var homeFormation: String? = null,
    @SerializedName("strAwayFormation")
    var awayFormation: String? = null,

    @SerializedName("strHomeGoalDetails")
    var homeGoalDetails: String? = null,
    @SerializedName("strAwayGoalDetails")
    var awayGoalDetails: String? = null,

    @SerializedName("intHomeShots")
    var homeShots: String? = null,
    @SerializedName("intAwayShots")
    var awayShots: String? = null,

    @SerializedName("strHomeLineupGoalkeeper")
    var homeGoalKeeper: String? = null,
    @SerializedName("strAwayLineupGoalkeeper")
    var awayGoalKeeper: String? = null,

    @SerializedName("strHomeLineupDefense")
    var homeLineupDefense: String? = null,
    @SerializedName("strAwayLineupDefense")
    var awayLineupDefense: String? = null,

    @SerializedName("strHomeLineupMidfield")
    var homeLineupMidfield: String? = null,
    @SerializedName("strAwayLineupMidfield")
    var awayLineupMidfield: String? = null,

    @SerializedName("strHomeLineupForward")
    var homeLineupForward: String? = null,
    @SerializedName("strAwayLineupForward")
    var awayLineupForward: String? = null,

    @SerializedName("strHomeLineupSubstitutes")
    var homeLineupSubstitutes: String? = null,
    @SerializedName("strAwayLineupSubstitutes")
    var awayLineupSubstitutes: String? = null,

    @SerializedName("idHomeTeam")
    var homeTeamId: String,
    @SerializedName("idAwayTeam")
    var awayTeamId: String,

    var homeBadge: String?,
    var awayBadge: String?
) : Parcelable