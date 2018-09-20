package com.dansuse.playwithkotlin.view.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.view.activity.OnItemClick
import com.dansuse.playwithkotlin.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter (private val events: List<Event>, private val clickHandler: OnItemClick) : RecyclerView.Adapter<MatchViewHolder>(){
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
    return MatchViewHolder(MatchUI().createView(AnkoContext.create(parent.context, parent)), clickHandler)
  }

  override fun getItemCount(): Int = events.size

  override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
    holder.bindItem(events[position])
  }
}

class MatchViewHolder(view: View, private val clickHandler: OnItemClick): RecyclerView.ViewHolder(view), View.OnClickListener{
  private val teamBadgeHome: ImageView = view.find(R.id.team_badge_home)
  private val teamNameHome: TextView = view.find(R.id.team_name_home)
  private val teamBadgeAway: ImageView = view.find(R.id.team_badge_away)
  private val teamNameAway: TextView = view.find(R.id.team_name_away)
  private val matchScore: TextView = view.find(R.id.match_score)
  private val matchDate: TextView = view.find(R.id.match_date)
  private val progressBarHomeBadge:ProgressBar = view.find(R.id.home_badge_progress_bar)
  private val progressBarAwayBadge:ProgressBar = view.find(R.id.away_badge_progress_bar)
  private lateinit var event:Event

  override fun onClick(v: View?) {
    clickHandler.onItemClick(event)
  }

  fun bindItem(event: Event){
    this.event = event
    itemView.setOnClickListener(this)

    val inputFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    val date:Date = inputFormat.parse(event.date)
    val outputFormat = SimpleDateFormat("E, dd MMM yyyy")

    if(event.homeBadge != null){
      progressBarHomeBadge.invisible()
      teamBadgeHome.visible()
      Picasso.get().load(event.homeBadge).into(teamBadgeHome)
    }else{
      progressBarHomeBadge.visible()
      teamBadgeHome.invisible()
    }

    if(event.awayBadge != null){
      progressBarAwayBadge.invisible()
      teamBadgeAway.visible()
      Picasso.get().load(event.awayBadge).into(teamBadgeAway)
    }else{
      progressBarAwayBadge.visible()
      teamBadgeAway.invisible()
    }

    teamNameHome.text = event.homeTeamName
    teamNameAway.text = event.awayTeamName
    matchDate.text = outputFormat.format(date)
    matchScore.text = itemView.context.getString(R.string.match_score, event.homeScore?:'-', event.awayScore?:'-')
  }
}

class MatchUI:AnkoComponent<ViewGroup>{
  override fun createView(ui: AnkoContext<ViewGroup>): View {
    return with(ui) {
      linearLayout {
        lparams(width = matchParent, height = wrapContent)
        padding = dip(16)
        orientation = LinearLayout.VERTICAL

        textView {
          id = R.id.match_date
          textSize = 16f
          gravity = Gravity.CENTER
        }.lparams{
          bottomMargin = dip(8)
          width = matchParent
        }

        linearLayout {
          lparams(width = matchParent, height = wrapContent)
          orientation = LinearLayout.HORIZONTAL
          gravity = Gravity.CENTER_VERTICAL

          teamUI(R.id.team_badge_home, R.id.team_name_home, R.id.home_badge_progress_bar)

          textView{
            id = R.id.match_score
            textSize = 16f
            gravity = Gravity.CENTER
          }.lparams(
              width = wrapContent,
              weight = 1.0f
          )
          teamUI(R.id.team_badge_away, R.id.team_name_away, R.id.away_badge_progress_bar)
        }
      }
    }
  }
  fun _LinearLayout.teamUI(idImageView:Int, idTextView:Int, idProgressBar:Int){
    linearLayout{
      orientation = LinearLayout.VERTICAL
      lparams(width = dip(0), height = wrapContent, weight = 2.0f)
      gravity = Gravity.CENTER_HORIZONTAL

      frameLayout {
        imageView {
          id = idImageView
          visibility = View.INVISIBLE
          scaleType = ImageView.ScaleType.CENTER_INSIDE
        }.lparams(
            width = dip(50), height = dip(50)
        ){
          gravity = Gravity.CENTER
        }
        progressBar {
          id = idProgressBar
        }.lparams{
          gravity = Gravity.CENTER
        }
      }

      textView{
        id = idTextView
        textSize = 12f
        gravity = Gravity.CENTER
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
      }.lparams(
          width = matchParent, height = wrapContent
      )
    }
  }
}
