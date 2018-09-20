package com.dansuse.playwithkotlin.view.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.R.color.colorAccent
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League
import com.dansuse.playwithkotlin.presenter.MainPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.MainView
import com.dansuse.playwithkotlin.view.adapter.MainAdapter
import com.dansuse.playwithkotlin.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MainActivity : AppCompatActivity(), MainView, OnItemClick {

  private lateinit var listTeam: RecyclerView
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var spinner: Spinner
  private lateinit var textViewErrorMessage:TextView

  private var events: MutableList<Event> = mutableListOf()
  private lateinit var presenter: MainPresenter
  private lateinit var adapter: MainAdapter
  private var leagueId: String? = null
  private var isPrevMatchMode:Boolean = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val states = arrayOf(
        intArrayOf(android.R.attr.state_checked), // enabled
        intArrayOf(-android.R.attr.state_checked) // disabled
    )

    val colors = intArrayOf(Color.WHITE, Color.LTGRAY)
    val colorStateList = ColorStateList(states, colors)

    relativeLayout {
      lparams (width = matchParent, height = matchParent){
        margin = dip(0)
      }
      padding = dip(0)

      linearLayout {
        lparams (width = matchParent, height = wrapContent)
        orientation = LinearLayout.VERTICAL
        topPadding = dip(16)

        spinner = spinner ().lparams{
          leftPadding = dip(16)
          rightPadding = dip(16)
        }
        swipeRefresh = swipeRefreshLayout {
          setColorSchemeResources(colorAccent,
              android.R.color.holo_green_light,
              android.R.color.holo_orange_light,
              android.R.color.holo_red_light)

          frameLayout{
            lparams (width = matchParent, height = wrapContent)

            listTeam = recyclerView {
              lparams (width = matchParent, height = wrapContent)
              layoutManager = LinearLayoutManager(ctx)
            }
            textViewErrorMessage = textView {
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
      }.lparams(width = matchParent, height = matchParent){
        topOf(R.id.bottom_navigation_bar)
      }

      bottomNavigationView {
        id = R.id.bottom_navigation_bar
        inflateMenu(R.menu.bottom_navigation_view)
        itemBackgroundResource = R.color.colorPrimary
        itemTextColor = colorStateList
        itemIconTintList = colorStateList
        padding = dip(0)
      }.lparams(width = matchParent, height = wrapContent){
        alignParentBottom()
        margin = dip(0)
      }.setOnNavigationItemSelectedListener{
          menuItem ->
            when(menuItem.itemId){
              R.id.action_prev_match -> {
                isPrevMatchMode = true
                presenter.get15EventsByLeagueId(leagueId ?:"", isPrevMatchMode)
                true
              }
              R.id.action_next_match -> {
                isPrevMatchMode = false
                presenter.get15EventsByLeagueId(leagueId?:"", isPrevMatchMode)
                true
              }
              else -> false
            }
          }
    }

    swipeRefresh.setOnRefreshListener {
      if(leagueId == null){
        presenter.getLeagueList()
      }else{
        presenter.get15EventsByLeagueId(leagueId?:"", isPrevMatchMode)
      }
    }

    adapter = MainAdapter(events, this)
    listTeam.adapter = adapter

    presenter = MainPresenter(this, TheSportDBApiService.create())
    presenter.getLeagueList()

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("tes123", "on item selected masuk")
        leagueId = (spinner.selectedItem as League).id
        if(isPrevMatchMode){
          presenter.get15EventsByLeagueId(leagueId ?: "", isPrevMatchMode)
        }else{
          presenter.get15EventsByLeagueId(leagueId ?: "", isPrevMatchMode)
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
  }

  override fun showLoading() {
    swipeRefresh.isRefreshing = true
  }

  override fun hideLoading() {
    swipeRefresh.isRefreshing = false
  }

  override fun showLeagueList(data: List<League>) {
    val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, data)
    spinner.adapter = spinnerAdapter
  }

  override fun showEventList(data: List<Event>) {
    hideLoading()
    textViewErrorMessage.invisible()
    listTeam.visible()
    events.clear()
    events.addAll(data)
    adapter.notifyDataSetChanged()
  }

  override fun showErrorMessage(error: String) {
    hideLoading()
    listTeam.invisible()
    textViewErrorMessage.visible()
    textViewErrorMessage.text = error
  }

  override fun onItemClick(event: Event) {
    startActivity<DetailActivity>("event" to event.id)
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }
}

interface OnItemClick{
  fun onItemClick(event:Event)
}
