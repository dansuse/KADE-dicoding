package com.dansuse.playwithkotlin.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.dansuse.playwithkotlin.EspressoIdlingResource
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.database.database
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.Favorite
import com.dansuse.playwithkotlin.presenter.DetailPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.DetailView
import com.dansuse.playwithkotlin.visible
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DetailView {

  companion object {
    var presenter: DetailPresenter? = null
  }

  private lateinit var presenter: DetailPresenter

  private lateinit var scrollView: ScrollView
  private lateinit var detailActivityProgressBar: ProgressBar

  private lateinit var matchDate:TextView
  private lateinit var matchScore:TextView
  private lateinit var homeTeamName:TextView
  private lateinit var awayTeamName:TextView
  private lateinit var homeBadge:ImageView
  private lateinit var awayBadge:ImageView
  private lateinit var homeBadgeProgressBar:ProgressBar
  private lateinit var awayBadgeProgressBar:ProgressBar
  private lateinit var homeFormation:TextView
  private lateinit var awayFormation:TextView

  private val goalsLabel:TextView by lazy {
    find<TextView>(R.id.goals_label_text)
  }
  private lateinit var homeGoals:TextView
  private lateinit var awayGoals:TextView

  private val shotsLabel:TextView by lazy {
    find<TextView>(R.id.shots_label_text)
  }
  private val homeShots:TextView by lazy {
    find<TextView>(R.id.home_shots)
  }
  private val awayShots:TextView by lazy {
    find<TextView>(R.id.away_shots)
  }

  private val goalKeeperLabel:TextView by lazy {
    find<TextView>(R.id.goal_keeper_label_text)
  }
  private val homeGoalKeeper:TextView by lazy {
    find<TextView>(R.id.home_goal_keeper)
  }
  private val awayGoalKeeper:TextView by lazy {
    find<TextView>(R.id.away_goal_keeper)
  }

  private val defenseLabel:TextView by lazy {
    find<TextView>(R.id.defense_label_text)
  }
  private val homeDefense:TextView by lazy {
    find<TextView>(R.id.home_defense)
  }
  private val awayDefense:TextView by lazy {
    find<TextView>(R.id.away_defense)
  }

  private val midfieldLabel:TextView by lazy {
    find<TextView>(R.id.midfield_label_text)
  }
  private val homeMidfield:TextView by lazy {
    find<TextView>(R.id.home_midfield)
  }
  private val awayMidfield:TextView by lazy {
    find<TextView>(R.id.away_midfield)
  }

  private val forwardLabel:TextView by lazy {
    find<TextView>(R.id.forward_label_text)
  }
  private val homeForward:TextView by lazy {
    find<TextView>(R.id.home_forward)
  }
  private val awayForward:TextView by lazy {
    find<TextView>(R.id.away_forward)
  }

  private val substitutesLabel:TextView by lazy {
    find<TextView>(R.id.substitutes_label_text)
  }
  private val homeSubstitutes:TextView by lazy {
    find<TextView>(R.id.home_substitutes)
  }
  private val awaySubstitutes:TextView by lazy {
    find<TextView>(R.id.away_substitutes)
  }

  private val textViewErrorMessage by lazy{
    find<TextView>(R.id.error_message)
  }

  private val frameLayout by lazy{
    find<FrameLayout>(R.id.detail_view)
  }

  private var menuItem: Menu? = null
  private var isFavorite: Boolean = false
  private var event:Event? = null
  private lateinit var eventId:String



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DetailActivityUI().setContentView(this)

    matchDate = find(R.id.match_date)
    matchScore = find(R.id.match_score)
    homeTeamName = find(R.id.team_name_home)
    awayTeamName = find(R.id.team_name_away)
    homeFormation = find(R.id.home_formation)
    awayFormation = find(R.id.away_formation)
    
    homeBadge = find(R.id.team_badge_home)
    awayBadge = find(R.id.team_badge_away)
    homeBadgeProgressBar = find(R.id.home_badge_progress_bar)
    awayBadgeProgressBar = find(R.id.away_badge_progress_bar)

    homeGoals = find(R.id.home_goals)
    awayGoals = find(R.id.away_goals)

    detailActivityProgressBar = find(R.id.detail_activity_progress_bar)
    scrollView = find(R.id.detail_activity_scroll_view)

    initPresenter()

    val intent = intent
    eventId = intent.getStringExtra("event")
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    favoriteState()
    presenter.getEventDetailById(eventId)

  }

  private fun initPresenter(){
    Companion.presenter?.let { this.presenter = it }
    if(this::presenter.isInitialized){
      return
    }
    presenter = DetailPresenter(this, TheSportDBApiService.create(), Schedulers.io(), AndroidSchedulers.mainThread())
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.detail_menu, menu)
    menuItem = menu
    setFavorite()
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      R.id.add_to_favorite -> {
        if (isFavorite) removeFromFavorite() else addToFavorite()

        setFavorite()
        true
      }

      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun setFavorite() {
    if (isFavorite)
      menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
    else
      menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
  }

  private fun favoriteState(){
    EspressoIdlingResource.mCountingIdlingResource.increment()
    database.use {
      val result = select(Favorite.TABLE_FAVORITE)
              .whereArgs("(${Favorite.EVENT_ID} = {id})",
                      "id" to eventId)
      val favorite = result.parseList(classParser<Favorite>())
      if (!favorite.isEmpty()) isFavorite = true
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  private fun addToFavorite(){
    EspressoIdlingResource.mCountingIdlingResource.increment()
    if(event != null){
      try {
        database.use {
          insert(Favorite.TABLE_FAVORITE,
                  Favorite.EVENT_ID to event?.id,
                  Favorite.EVENT_DATE to event?.date,
                  Favorite.HOME_SCORE to event?.homeScore,
                  Favorite.AWAY_SCORE to event?.awayScore,
                  Favorite.HOME_NAME to event?.homeTeamName,
                  Favorite.AWAY_NAME to event?.awayTeamName,
                  Favorite.HOME_BADGE to event?.homeBadge,
                  Favorite.AWAY_BADGE to event?.awayBadge
                  )
        }
        isFavorite = true
        frameLayout.snackbar(getString(R.string.event_added_to_favorites)).show()
      } catch (e: SQLiteConstraintException){
        frameLayout.snackbar(e.localizedMessage).show()
      } finally {

      }
    }else{
      frameLayout.snackbar(getString(R.string.event_is_still_loading)).show()
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  private fun removeFromFavorite(){
    EspressoIdlingResource.mCountingIdlingResource.increment()
    try {
      database.use {
        delete(Favorite.TABLE_FAVORITE, "(${Favorite.EVENT_ID} = {id})",
                "id" to eventId)
      }
      isFavorite = false
      frameLayout.snackbar(getString(R.string.event_removed_from_favorites)).show()
    } catch (e: SQLiteConstraintException){
      frameLayout.snackbar(e.localizedMessage).show()
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }

  override fun showLoading() {
    scrollView.invisible()
    textViewErrorMessage.invisible()
    detailActivityProgressBar.visible()
  }

  override fun hideLoading() {
    detailActivityProgressBar.invisible()
    textViewErrorMessage.invisible()
    scrollView.visible()
  }

  override fun showErrorMessage(error: String) {
    detailActivityProgressBar.invisible()
    scrollView.invisible()
    textViewErrorMessage.visible()
    textViewErrorMessage.text = error
  }

  override fun showEventDetail(event: Event) {
    this.event = event
    matchScore.text = getString(R.string.match_score, event.homeScore?:'-', event.awayScore?:'-')
    homeTeamName.text = event.homeTeamName
    awayTeamName.text = event.awayTeamName
    homeFormation.text = if(event.homeFormation.isNullOrEmpty()) "-" else event.homeFormation
    awayFormation.text = if(event.awayFormation.isNullOrEmpty()) "-" else event.awayFormation

    goalsLabel.text = getString(R.string.goals)
    homeGoals.text = event.homeGoalDetails?.replace(";", "\n")
    awayGoals.text = event.awayGoalDetails?.replace(";", "\n")

    shotsLabel.text = getString(R.string.shots)
    homeShots.text = event.homeShots?:"-"
    awayShots.text = event.awayShots?:"-"

    goalKeeperLabel.text = getString(R.string.goal_keeper)
    homeGoalKeeper.text = event.homeGoalKeeper?.replace(";", "\n")
    awayGoalKeeper.text = event.awayGoalKeeper?.replace(";", "\n")

    defenseLabel.text = getString(R.string.defense)
    homeDefense.text = event.homeLineupDefense?.replace(";", "\n")
    awayDefense.text = event.awayLineupDefense?.replace(";", "\n")

    midfieldLabel.text = getString(R.string.midfield)
    homeMidfield.text = event.homeLineupMidfield?.replace(";", "\n")
    awayMidfield.text = event.awayLineupMidfield?.replace(";", "\n")

    forwardLabel.text = getString(R.string.forward)
    homeForward.text = event.homeLineupForward?.replace(";", "\n")
    awayForward.text = event.awayLineupForward?.replace(";", "\n")

    substitutesLabel.text = getString(R.string.substitutes)
    homeSubstitutes.text = event.homeLineupSubstitutes?.replace(";", "\n")
    awaySubstitutes.text = event.awayLineupSubstitutes?.replace(";", "\n")

    if(event.homeBadge != null){
      homeBadgeProgressBar.invisible()
      homeBadge.visible()
      Picasso.get().load(event.homeBadge).into(homeBadge)
    }
    if(event.awayBadge != null){
      awayBadgeProgressBar.invisible()
      awayBadge.visible()
      Picasso.get().load(event.awayBadge).into(awayBadge)
    }
    val inputFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    val date: Date = inputFormat.parse(event.date)
    val outputFormat = SimpleDateFormat("E, dd MMM yyyy")
    matchDate.text = outputFormat.format(date)
  }

}

class DetailActivityUI:AnkoComponent<DetailActivity>{
  override fun createView(ui: AnkoContext<DetailActivity>): View {
    return with(ui) {
      frameLayout {
        lparams(width = matchParent, height = matchParent)
        id = R.id.detail_view
        scrollView {
          id=R.id.detail_activity_scroll_view
          visibility = View.INVISIBLE
          lparams(width = matchParent, height = matchParent)
          linearLayout {
            orientation = LinearLayout.VERTICAL
            lparams(width = matchParent, height = wrapContent)
            textView{
              id = R.id.match_date
              textSize = 16f
              setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }.lparams {
              gravity = Gravity.CENTER_HORIZONTAL
              topMargin = dip(16)
              bottomMargin = dip(16)
            }
            linearLayout {
              orientation = LinearLayout.HORIZONTAL
              lparams(width = matchParent, height = wrapContent)

              teamUI(R.id.team_badge_home, R.id.team_name_home, R.id.home_badge_progress_bar, R.id.home_formation)
              textView{
                id = R.id.match_score
                textSize = 20f
              }
              teamUI(R.id.team_badge_away, R.id.team_name_away, R.id.away_badge_progress_bar, R.id.away_formation)
            }

            separator()
            defaultInformationUI(R.id.home_goals, R.id.goals_label_text, R.id.away_goals)
            defaultInformationUI(R.id.home_shots, R.id.shots_label_text, R.id.away_shots)

            separator()
            textView(context.getString(R.string.lineups)){
              gravity = Gravity.CENTER_HORIZONTAL
              textSize = 16f
            }.lparams(width= matchParent, height = wrapContent)

            defaultInformationUI(R.id.home_goal_keeper, R.id.goal_keeper_label_text, R.id.away_goal_keeper)
            defaultInformationUI(R.id.home_defense, R.id.defense_label_text, R.id.away_defense)
            defaultInformationUI(R.id.home_midfield, R.id.midfield_label_text, R.id.away_midfield)
            defaultInformationUI(R.id.home_forward, R.id.forward_label_text, R.id.away_forward)
            defaultInformationUI(R.id.home_substitutes, R.id.substitutes_label_text, R.id.away_substitutes)
          }
        }
        progressBar {
          id = R.id.detail_activity_progress_bar
        }.lparams(
            width = wrapContent, height = wrapContent
        ){
          gravity=Gravity.CENTER
        }
        textView{
          id = R.id.error_message
          textSize = 14f
          visibility = View.INVISIBLE
          gravity = Gravity.CENTER
        }.lparams(width = matchParent, height = wrapContent){
          gravity = Gravity.CENTER
          marginStart = dip(8)
          marginEnd = dip(8)
        }
      }
    }
  }

  fun @AnkoViewDslMarker _LinearLayout.defaultInformationUI(idHomeInformation:Int, idLabelInformation:Int, idAwayInformation:Int) {
    linearLayout {
      orientation = LinearLayout.HORIZONTAL
      lparams(width = matchParent, height = wrapContent)

      textView {
        id = idHomeInformation
        setPaddingRelative(dip(8), 0, 0, 0)
      }.lparams(width = dip(0), height = wrapContent, weight = 2.0f)

      textView {
        id = idLabelInformation
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
      }.lparams(
          width = wrapContent, height = wrapContent
      ) {
        marginEnd = dip(8)
        marginStart = dip(8)
      }

      textView {
        id = idAwayInformation
        textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        setPaddingRelative(0, 0, dip(8), 0)
      }.lparams(width = dip(0), height = wrapContent, weight = 2.0f)
    }.lparams(width = matchParent, height = wrapContent){
      bottomMargin = dip(8)
    }
  }

  fun @AnkoViewDslMarker _LinearLayout.separator() {
    view {
      background = ColorDrawable(Color.LTGRAY)
    }.lparams(
        width = matchParent, height = dip(4)
    )
  }

  fun _LinearLayout.teamUI(idImageView:Int, idTextView:Int, idProgressBar:Int, idTeamFormation:Int){
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
        setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
      }.lparams(
          width = matchParent, height = wrapContent
      )
      textView {
        id = idTeamFormation
      }.lparams{
        gravity = Gravity.CENTER_HORIZONTAL
      }
    }
  }
}
