package com.dansuse.playwithkotlin.model

data class Favorite(val id: Long?, val eventId: String?, val eventDate: String?,
                    val homeScore: String?, val awayScore: String?,
                    val homeName: String?, val awayName: String?,
                    val homeBadge: String?, val awayBadge: String?) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_DATE: String = "EVENT_DATE"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val HOME_NAME: String = "HOME_NAME"
        const val AWAY_NAME: String = "AWAY_NAME"
        const val HOME_BADGE: String = "HOME_BADGE"
        const val AWAY_BADGE: String = "AWAY_BADGE"
    }
}