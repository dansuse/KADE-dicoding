package com.dansuse.playwithkotlin.view.teamdetail

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dansuse.playwithkotlin.EspressoIdlingResource
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.database.database
import com.dansuse.playwithkotlin.model.FavoriteTeam
import com.dansuse.playwithkotlin.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.delete
import com.dansuse.playwithkotlin.presenter.teamdetail.TeamDetailPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.teamdetail.overview.TeamOverviewView
import com.dansuse.playwithkotlin.view.teamdetail.players.PlayersView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.viewPager


class TeamDetailActivity : AppCompatActivity(), TeamDetailView {

  private lateinit var toolbar: Toolbar

  //private lateinit var progressBar: ProgressBar
  //private lateinit var swipeRefresh: SwipeRefreshLayout

  private lateinit var tabAdapter: TeamDetailTabAdapter
  private lateinit var tabLayout: TabLayout
  private lateinit var viewPager: ViewPager

  private lateinit var coordinatorLayout: CoordinatorLayout
  private lateinit var teamBadge: ImageView
  private lateinit var teamName: TextView
  private lateinit var teamFormedYear: TextView
  private lateinit var teamStadium: TextView
  private lateinit var teamDescription: TextView

  private lateinit var presenter: TeamDetailPresenter
  private var teams: Team? = null
  private lateinit var id: String

  private var menuItem: Menu? = null
  private var isFavorite: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    coordinatorLayout = coordinatorLayout {
      lparams(width = matchParent, height = matchParent)
      fitsSystemWindows = true

      appBarLayout {
        id = R.id.appbar
        setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
        fitsSystemWindows = true

        collapsingToolbarLayout {
          id = R.id.collapsing_toolbar
          fitsSystemWindows = true
          isTitleEnabled = false
          setContentScrimColor(ContextCompat.getColor(context, R.color.colorPrimary))
          setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))

          linearLayout {
            padding = dip(10)
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))

            teamBadge = imageView {}.lparams(height = dip(75))

            teamName = textView {
              this.gravity = Gravity.CENTER
              textSize = 20f
              textColor = Color.WHITE
            }.lparams {
              topMargin = dip(5)
            }

            teamFormedYear = textView {
              this.gravity = Gravity.CENTER
              textColor = Color.WHITE
            }

            teamStadium = textView {
              this.gravity = Gravity.CENTER
              textColor = Color.WHITE
            }
          }.lparams(width = matchParent, height = wrapContent){
            setMargins(dip(0), dip(75), dip(0), dip(30))
          }

          toolbar = toolbar {
            id = R.id.toolbar
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
          }.lparams(height = dimenAttr(R.attr.actionBarSize), width = matchParent)
          {
//            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            gravity = Gravity.TOP
          }

          tabLayout = tabLayout {
            id = R.id.tab_layout_detail_team
            tabGravity = TabLayout.GRAVITY_FILL
            tabMode = TabLayout.MODE_FIXED
            minimumHeight = dimenAttr(R.attr.actionBarSize)
            setTabTextColors(Color.LTGRAY, Color.WHITE)
            setSelectedTabIndicatorColor(Color.WHITE)
            setBackgroundResource(R.color.colorPrimary)
          }.lparams (width = matchParent, height = wrapContent) {
            gravity = Gravity.BOTTOM
            collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
          }

        }.lparams(width = matchParent, height = wrapContent){
          scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }

      }.lparams(width = matchParent, height = wrapContent)

      viewPager = viewPager {
        id = R.id.view_pager_team_detail
      }.lparams(width = matchParent, height = matchParent){
        behavior = AppBarLayout.ScrollingViewBehavior()
      }

//      nestedScrollView {
//
//      }.lparams(width = matchParent, height = matchParent){
//        behavior = AppBarLayout.ScrollingViewBehavior()
//      }
    }

//    linearLayout {
//      lparams(width = matchParent, height = wrapContent)
//      orientation = LinearLayout.VERTICAL
//      backgroundColor = Color.WHITE
//
//      swipeRefresh = swipeRefreshLayout {
//        setColorSchemeResources(colorAccent,
//            android.R.color.holo_green_light,
//            android.R.color.holo_orange_light,
//            android.R.color.holo_red_light)
//
//        scrollView {
//          isVerticalScrollBarEnabled = false
//          relativeLayout {
//            lparams(width = matchParent, height = wrapContent)
//
//            linearLayout {
//              lparams(width = matchParent, height = wrapContent)
//              padding = dip(10)
//              orientation = LinearLayout.VERTICAL
//              gravity = Gravity.CENTER_HORIZONTAL
//
//              teamBadge = imageView {}.lparams(height = dip(75))
//
//              teamName = textView {
//                this.gravity = Gravity.CENTER
//                textSize = 20f
//                textColor = ContextCompat.getColor(context, colorAccent)
//              }.lparams {
//                topMargin = dip(5)
//              }
//
//              teamFormedYear = textView {
//                this.gravity = Gravity.CENTER
//              }
//
//              teamStadium = textView {
//                this.gravity = Gravity.CENTER
//                textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
//              }
//
//              teamDescription = textView().lparams {
//                topMargin = dip(20)
//              }
//            }
//            progressBar = progressBar {
//            }.lparams {
//              centerHorizontally()
//            }
//          }
//        }
//      }
//    }

    setSupportActionBar(toolbar)
    supportActionBar?.title = "Team Detail"
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val intent = intent
    id = intent.getStringExtra("id")
    Log.d("tes123", "masuk on Create")
    favoriteState()
    invalidateOptionsMenu()

    tabAdapter = TeamDetailTabAdapter(supportFragmentManager, id)
    viewPager.adapter = tabAdapter
    tabLayout.setupWithViewPager(viewPager)
//    tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
//      override fun onTabReselected(p0: TabLayout.Tab?) {
//
//      }
//
//      override fun onTabUnselected(p0: TabLayout.Tab?) {
//
//      }
//
//      override fun onTabSelected(tab: TabLayout.Tab?) {
////        if(tab?.position != null) {
////          viewPager.currentItem = tab?.position!!
////        }
////        if(tab?.position == 1){
////          if(teams.teamId != null){
////            presenter.getPlayersByTeamId(teams.teamId!!)
////          }
////        }else if(tab?.position == 0){
////
////        }
//      }
//    })

    presenter = TeamDetailPresenter(this, tabAdapter.getItem(0) as TeamOverviewView, tabAdapter.getItem(1) as PlayersView, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
    presenter.getTeamDetail(id)
//    swipeRefresh.onRefresh {
//      presenter.getTeamDetail(id)
//    }


  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //Log.d("tes123", "masuk on create option menu")
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

  private fun favoriteState() {
    EspressoIdlingResource.mCountingIdlingResource.increment()
    database.use {
      val result = select(FavoriteTeam.TABLE_FAVORITE)
          .whereArgs("(TEAM_ID = {id})",
              "id" to id)
      val favorite = result.parseList(classParser<FavoriteTeam>())
      if (!favorite.isEmpty()) isFavorite = true
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  private fun setFavorite() {
    if (isFavorite)
      menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
    else
      menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
  }

  private fun addToFavorite() {
    EspressoIdlingResource.mCountingIdlingResource.increment()
    if(teams!=null){
      try {
        database.use {
          insert(FavoriteTeam.TABLE_FAVORITE,
              FavoriteTeam.TEAM_ID to teams?.teamId,
              FavoriteTeam.TEAM_NAME to teams?.teamName,
              FavoriteTeam.TEAM_BADGE to teams?.teamBadge)
        }
        isFavorite = true
        coordinatorLayout.snackbar("Added to favorite").show()
      } catch (e: SQLiteConstraintException) {
        coordinatorLayout.snackbar(e.message ?: "Terjadi error saat menambahkan team ke daftar favorit").show()
      }
    }else{
      coordinatorLayout.snackbar(getString(R.string.event_is_still_loading)).show()
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  private fun removeFromFavorite() {
    EspressoIdlingResource.mCountingIdlingResource.increment()
    try {
      database.use {
        delete(FavoriteTeam.TABLE_FAVORITE, "(TEAM_ID = {id})", "id" to id)
      }
      isFavorite = false
      coordinatorLayout.snackbar("Removed to favorite").show()
    } catch (e: SQLiteConstraintException) {
      coordinatorLayout.snackbar(e.message ?: "Terjadi error saat remove team dari daftar favorit").show()
    }
    EspressoIdlingResource.mCountingIdlingResource.decrement()
  }

  override fun showLoading() {
    //progressBar.visible()
  }

  override fun hideLoading() {
    //progressBar.invisible()
  }

  override fun showTeamDetail(data: List<Team>) {
    //swipeRefresh.isRefreshing = false
    if (data.isNotEmpty()) {
      teams = data[0]
      Picasso.get().load(teams?.teamBadge).into(teamBadge)
      teamName.text = teams?.teamName
      teamFormedYear.text = teams?.teamFormedYear
      teamStadium.text = teams?.teamStadium
//      tabAdapter.teamId = teams.teamId
//      teamDescription.text = teams.teamDescription
//      val teamOverviewView = (tabAdapter.getItem(0) as TeamOverviewView)
//      teamOverviewView.hideLoading()
//      teamOverviewView.showTeamDescription(teams.teamDescription?:"")
    }
  }

  override fun showErrorMessage(errorMessage: String) {

  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }
}
