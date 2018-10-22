package com.dansuse.playwithkotlin.model

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("idPlayer")
    var playerId: String? = null,

    @SerializedName("strPlayer")
    var playerName: String? = null,

    @SerializedName("strWeight")
    var weight: String? = null,

    @SerializedName("strHeight")
    var height: String? = null,

    @SerializedName("strPosition")
    var position: String? = null,

    @SerializedName("strDescriptionEN")
    var description: String? = null,

    @SerializedName("strThumb")
    var urlThumbnail: String? = null,

    @SerializedName("strCutout")
    var urlCutout: String? = null
)