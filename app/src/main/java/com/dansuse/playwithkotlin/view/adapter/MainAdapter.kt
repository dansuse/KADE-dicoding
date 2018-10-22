package com.dansuse.playwithkotlin.view.adapter

import android.content.Intent
import android.graphics.Color
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dansuse.playwithkotlin.*
import com.dansuse.playwithkotlin.model.Event
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter(
    private val isPrevMatchMode:Boolean,
    private val events: List<Event>,
    private val listener: (Event) -> Unit) : RecyclerView.Adapter<MatchViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
    return MatchViewHolder(MatchUI().createView(AnkoContext.create(parent.context, parent)))
  }

  override fun getItemCount(): Int = events.size

  override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
    holder.bindItem(isPrevMatchMode, events[position], listener)
  }
}

class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  private val teamBadgeHome: ImageView = view.find(R.id.team_badge_home)
  private val teamNameHome: TextView = view.find(R.id.team_name_home)
  private val teamBadgeAway: ImageView = view.find(R.id.team_badge_away)
  private val teamNameAway: TextView = view.find(R.id.team_name_away)
  private val matchScore: TextView = view.find(R.id.match_score)
  private val matchDate: TextView = view.find(R.id.match_date)
  private val matchTime: TextView = view.find(R.id.match_time)
  private val buttonAddToCalendar: ImageButton = view.find(R.id.add_to_calendar)
  private val progressBarHomeBadge: ProgressBar = view.find(R.id.home_badge_progress_bar)
  private val progressBarAwayBadge: ProgressBar = view.find(R.id.away_badge_progress_bar)
  private lateinit var event: Event

  private val inputFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
  private val outputFormat = SimpleDateFormat("E, dd MMM yyyy")
  private val inputTimeFormat = SimpleDateFormat("HH:mm:ssXXX")
  private val outputTimeFormat = SimpleDateFormat("HH:mm")
  val inputFormatForCalendar = SimpleDateFormat("E, dd MMM yyyy HH:mm")
  init {
    outputFormat.timeZone = TimeZone.getTimeZone("Asia/Pontianak")
    outputTimeFormat.timeZone = TimeZone.getTimeZone("Asia/Pontianak")
    inputFormatForCalendar.timeZone = TimeZone.getTimeZone("Asia/Pontianak")
  }

  fun bindItem(isPrevMatchMode: Boolean, event: Event, listener: (Event) -> Unit) {
    this.event = event
    itemView.onClick {
      listener(event)
    }


    val date: Date? = try{
      inputFormat.parse(event.date)
    }catch (e:Exception){
      null
    }
    val inputTime :Date? = try{
      inputTimeFormat.parse(event.time)
    }catch (e:Exception){
      null
    }
    buttonAddToCalendar.enable()
    if(isPrevMatchMode){
      buttonAddToCalendar.invisible()
    }else{
      buttonAddToCalendar.visible()
      if(date == null || inputTime == null){
        buttonAddToCalendar.disable()
      }else{
        buttonAddToCalendar.onClick {
          itemView.context.startActivity(getIntentForAddingToCalendar(date, inputTime))
        }
      }
    }

    if (event.homeBadge != null) {
      progressBarHomeBadge.invisible()
      teamBadgeHome.visible()
      Picasso.get().load(event.homeBadge).into(teamBadgeHome)
    } else {
      progressBarHomeBadge.visible()
      teamBadgeHome.invisible()
    }

    if (event.awayBadge != null) {
      progressBarAwayBadge.invisible()
      teamBadgeAway.visible()
      Picasso.get().load(event.awayBadge).into(teamBadgeAway)
    } else {
      progressBarAwayBadge.visible()
      teamBadgeAway.invisible()
    }

    teamNameHome.text = event.homeTeamName
    teamNameAway.text = event.awayTeamName
    matchDate.text = if(date == null) "-" else outputFormat.format(date)
    matchTime.text = if(inputTime == null) "-" else outputTimeFormat.format(inputTime)
    matchScore.text = itemView.context.getString(R.string.match_score, event.homeScore
        ?: '-', event.awayScore ?: '-')
  }

  fun getIntentForAddingToCalendar(date:Date, time:Date) : Intent{
    val dateForCalendar:Date = getEventDateForAddingToCalendar(date, time)
    val beginTime:Calendar = Calendar.getInstance()
    beginTime.timeZone = TimeZone.getTimeZone("Asia/Pontianak")
    beginTime.time = dateForCalendar
    val endTime:Calendar = Calendar.getInstance()
    endTime.timeZone = TimeZone.getTimeZone("Asia/Pontianak")
    beginTime.time = dateForCalendar

    val intent:Intent = Intent(Intent.ACTION_INSERT).apply {
      data = CalendarContract.Events.CONTENT_URI
      putExtra(CalendarContract.Events.TITLE, event.title)
      putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
      putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
      putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
      putExtra(CalendarContract.Events.HAS_ALARM, 1)
      putExtra(CalendarContract.Events.MAX_REMINDERS, 1)
      putExtra(CalendarContract.Reminders.MINUTES, 30)
    }
    return intent
  }

  fun getEventDateForAddingToCalendar(date:Date, time:Date):Date{
    var dateStringForCalendar = outputFormat.format(date)
    dateStringForCalendar += " " + outputTimeFormat.format(time)
    return inputFormatForCalendar.parse(dateStringForCalendar)
  }
}

class MatchUI : AnkoComponent<ViewGroup> {
  override fun createView(ui: AnkoContext<ViewGroup>): View {
    return with(ui) {
      cardView {
        lparams(width = matchParent, height = wrapContent){
          topMargin = dip(8)
          bottomMargin = dip(8)
        }
        linearLayout {
          padding = dip(16)
          orientation = LinearLayout.VERTICAL

          textView {
            id = R.id.match_date
            textSize = 16f
            gravity = Gravity.CENTER
          }.lparams {
            bottomMargin = dip(8)
            width = matchParent
          }

          textView {
            id = R.id.match_time
            textSize = 16f
            gravity = Gravity.CENTER
          }.lparams {
            bottomMargin = dip(8)
            width = matchParent
          }

          linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL

            teamUI(R.id.team_badge_home, R.id.team_name_home, R.id.home_badge_progress_bar)

            textView {
              id = R.id.match_score
              textSize = 16f
              gravity = Gravity.CENTER
            }.lparams(
                width = wrapContent,
                weight = 1.0f
            )
            teamUI(R.id.team_badge_away, R.id.team_name_away, R.id.away_badge_progress_bar)
          }
        }.lparams(width= matchParent, height = wrapContent)
        imageButton {
          id = R.id.add_to_calendar
          setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add_alarm_black_24dp))
          backgroundColor = Color.WHITE
          onClick {

          }
        }.lparams(width = dip(30), height = dip(30)){
          gravity = (Gravity.TOP or Gravity.END)
        }
      }
    }
  }

  fun _LinearLayout.teamUI(idImageView: Int, idTextView: Int, idProgressBar: Int) {
    linearLayout {
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
        ) {
          gravity = Gravity.CENTER
        }
        progressBar {
          id = idProgressBar
        }.lparams {
          gravity = Gravity.CENTER
        }
      }

      textView {
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
