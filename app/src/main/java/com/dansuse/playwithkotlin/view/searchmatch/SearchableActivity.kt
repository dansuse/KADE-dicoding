package com.dansuse.playwithkotlin.view.searchmatch

import android.app.SearchManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.presenter.MainPresenter
import com.dansuse.playwithkotlin.presenter.SearchMatchPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.activity.DetailActivity
import com.dansuse.playwithkotlin.view.adapter.MainAdapter
import com.dansuse.playwithkotlin.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class SearchableActivity : AppCompatActivity(), SearchMatchView,
SearchView.OnQueryTextListener{

  companion object {
    var presenter: SearchMatchPresenter? = null
  }

  private lateinit var listTeam: RecyclerView
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var textViewErrorMessage: TextView

  private var events: MutableList<Event> = mutableListOf()
  private lateinit var presenter: SearchMatchPresenter
  private lateinit var adapter: MainAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    linearLayout {
      lparams(width = matchParent, height = matchParent)
      orientation = LinearLayout.VERTICAL
      topPadding = dip(16)

      swipeRefresh = swipeRefreshLayout {
        setColorSchemeResources(R.color.colorAccent,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        frameLayout {
          lparams(width = matchParent, height = matchParent)

          listTeam = recyclerView {
            id = R.id.list_event
            lparams(width = matchParent, height = matchParent)
            layoutManager = LinearLayoutManager(ctx)
          }
          textViewErrorMessage = textView {
            textSize = 14f
            visibility = View.INVISIBLE
            gravity = Gravity.CENTER
          }.lparams(width = matchParent, height = wrapContent) {
            gravity = Gravity.CENTER
            marginStart = dip(8)
            marginEnd = dip(8)
          }
        }
      }
    }

    adapter = MainAdapter(true, events) {
      this.startActivity<DetailActivity>("event" to it.id)
    }
    listTeam.adapter = adapter
    initPresenter()
//    if(Intent.ACTION_SEARCH == intent.action){
//      intent.getStringExtra(SearchManager.QUERY)?.also{query->doSearchMatch(query)}
//    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.search_menu, menu)
    val menuItem = menu?.findItem(R.id.action_search)
    val searchView = menuItem?.actionView as SearchView
    searchView.setOnQueryTextListener(this)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return super.onOptionsItemSelected(item)
  }

  override fun onQueryTextSubmit(p0: String?): Boolean {
    return false
  }

  override fun onQueryTextChange(query: String?): Boolean {
    if(query?.length ?: 0 > 3){
      doSearchMatch(query?:"")
    }
    return true
  }

  private fun initPresenter() {
    Companion.presenter?.let { this.presenter = it }
    if (this::presenter.isInitialized) {
      return
    }
    presenter = SearchMatchPresenter(this, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
  }

  fun doSearchMatch(query:String){
    presenter.getSeachResult(query)
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }

  override fun showLoading() {
    swipeRefresh.isRefreshing = true
  }

  override fun hideLoading() {
    swipeRefresh.isRefreshing = false
  }

  override fun showSearchResult(data: List<Event>) {
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
}
