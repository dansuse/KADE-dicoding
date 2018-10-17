package com.dansuse.playwithkotlin.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.FavoriteMatch
import com.dansuse.playwithkotlin.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class FavoriteTeamsAdapter(private val favoriteMatch: List<FavoriteMatch>, private val listener: (FavoriteMatch) -> Unit)
  : RecyclerView.Adapter<FavoriteViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
    return FavoriteViewHolder(MatchUI().createView(AnkoContext.create(parent.context, parent)))
  }

  override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
    holder.bindItem(favoriteMatch[position], listener)
  }

  override fun getItemCount(): Int = favoriteMatch.size

}

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

  private val teamBadgeHome: ImageView = view.find(R.id.team_badge_home)
  private val teamNameHome: TextView = view.find(R.id.team_name_home)
  private val teamBadgeAway: ImageView = view.find(R.id.team_badge_away)
  private val teamNameAway: TextView = view.find(R.id.team_name_away)
  private val matchScore: TextView = view.find(R.id.match_score)
  private val matchDate: TextView = view.find(R.id.match_date)
  private val progressBarHomeBadge: ProgressBar = view.find(R.id.home_badge_progress_bar)
  private val progressBarAwayBadge: ProgressBar = view.find(R.id.away_badge_progress_bar)
  private lateinit var favoriteMatch: FavoriteMatch

  fun bindItem(favoriteMatch: FavoriteMatch, listener: (FavoriteMatch) -> Unit) {
    this.favoriteMatch = favoriteMatch
    itemView.onClick {
      listener(favoriteMatch)
    }

    val inputFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    val date: Date = inputFormat.parse(favoriteMatch.eventDate)
    val outputFormat = SimpleDateFormat("E, dd MMM yyyy")

    if (favoriteMatch.homeBadge != null) {
      progressBarHomeBadge.invisible()
      teamBadgeHome.visible()
      Picasso.get().load(favoriteMatch.homeBadge).into(teamBadgeHome)
    } else {
      progressBarHomeBadge.visible()
      teamBadgeHome.invisible()
    }

    if (favoriteMatch.awayBadge != null) {
      progressBarAwayBadge.invisible()
      teamBadgeAway.visible()
      Picasso.get().load(favoriteMatch.awayBadge).into(teamBadgeAway)
    } else {
      progressBarAwayBadge.visible()
      teamBadgeAway.invisible()
    }

    teamNameHome.text = favoriteMatch.homeName
    teamNameAway.text = favoriteMatch.awayName
    matchDate.text = outputFormat.format(date)
    matchScore.text = itemView.context.getString(R.string.match_score, favoriteMatch.homeScore
        ?: '-', favoriteMatch.awayScore ?: '-')
  }
}