package com.dansuse.playwithkotlin.model

import com.google.gson.annotations.SerializedName

data class League(
    @SerializedName("idLeague")
    var id: String,
    @SerializedName("strLeague")
    var leagueName: String? = null
){
    override fun toString(): String {
        return leagueName?: ""
    }
}