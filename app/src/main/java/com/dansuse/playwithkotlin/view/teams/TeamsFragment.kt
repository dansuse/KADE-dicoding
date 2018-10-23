package com.dansuse.playwithkotlin.view.teams

import android.content.Context
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.*
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.League
import com.dansuse.playwithkotlin.model.Team
import com.dansuse.playwithkotlin.presenter.TeamsPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.searchmatch.SearchableActivity
import com.dansuse.playwithkotlin.view.teamdetail.TeamDetailActivity
import com.dansuse.playwithkotlin.view.teams.TeamsView
import com.dansuse.playwithkotlin.view.teams.TeamsAdapter
import com.dansuse.playwithkotlin.visible
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class TeamsFragment : Fragment(), AnkoComponent<Context>, TeamsView,
    SearchView.OnQueryTextListener {

  private lateinit var listTeam: RecyclerView
  //private lateinit var progressBar: ProgressBar
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var spinner: Spinner

  private var teams: MutableList<Team> = mutableListOf()
  private lateinit var presenter: TeamsPresenter
  private lateinit var adapter: TeamsAdapter
  private var leagueName: String? = null

  private val idlingRes: CountingIdlingResource = CountingIdlingResource("TeamsFragment")

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    adapter = TeamsAdapter(teams) {
      requireContext().startActivity<TeamDetailActivity>("id" to "${it.teamId}")
    }
    listTeam.adapter = adapter


//    val spinnerItems = resources.getStringArray(R.array.league)
//    val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
//    spinner.adapter = spinnerAdapter

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        leagueName = spinner.selectedItem.toString()
        presenter.getTeamList(leagueName?:"")
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    swipeRefresh.setOnRefreshListener {
      if (leagueName == null) {
        presenter.getLeagueList()
      } else {
        presenter.getTeamList(leagueName ?: "")
      }
    }

    presenter = TeamsPresenter(this, TheSportDBApiService.create(), Schedulers.io(), AndroidSchedulers.mainThread())
    presenter.getLeagueList()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    setHasOptionsMenu(true)
    return createView(AnkoContext.create(ctx))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui) {
    linearLayout {
      lparams(width = matchParent, height = matchParent)
      orientation = LinearLayout.VERTICAL
      topPadding = dip(16)
      leftPadding = dip(16)
      rightPadding = dip(16)

      spinner = spinner()
      swipeRefresh = swipeRefreshLayout {
        setColorSchemeResources(R.color.colorAccent,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        relativeLayout {
          lparams(width = matchParent, height = matchParent)

          listTeam = recyclerView {
            id = R.id.list_team
            lparams(width = matchParent, height = matchParent)
            layoutManager = LinearLayoutManager(ctx)
          }

//          progressBar = progressBar {
//          }.lparams {
//            centerHorizontally()
//          }
        }
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater?.inflate(R.menu.search_menu, menu)
    val menuItem = menu?.findItem(R.id.action_search)
    val searchView = menuItem?.actionView as SearchView
    searchView.setOnQueryTextListener(this)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//    return when(item?.itemId){
//      R.id.action_search -> {
//        context?.startActivity<SearchableActivity>()
//        true
//      }
//      else -> super.onOptionsItemSelected(item)
//    }
    return super.onOptionsItemSelected(item)
  }

  override fun onQueryTextSubmit(p0: String?): Boolean {
    return false
  }

  override fun onQueryTextChange(query: String?): Boolean {
    if(query?.length ?: 0 > 3){
      doSearchTeam(query?:"")
    }
    return true
  }

  fun doSearchTeam(query:String){
    presenter.getSearchResult(query)
  }

  override fun showLoading() {
    //progressBar.visible()
    swipeRefresh.isRefreshing = true
  }

  override fun hideLoading() {
    //progressBar.invisible()
    swipeRefresh.isRefreshing = false
  }

  override fun showLeagueList(data: List<League>) {
    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, data)
    spinner.adapter = spinnerAdapter
  }

  override fun showTeamList(data: List<Team>) {
    swipeRefresh.isRefreshing = false
    teams.clear()
    teams.addAll(data)
    adapter.notifyDataSetChanged()
  }

  override fun showErrorMessage(errorMessage: String) {

  }

  fun getIdlingResourceInTest(): CountingIdlingResource {
    return idlingRes
  }
}