package com.dansuse.playwithkotlin.view.matches

import android.content.Context
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Event
import com.dansuse.playwithkotlin.model.League
import com.dansuse.playwithkotlin.presenter.MatchesPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.view.matchdetail.MatchDetailActivity
import com.dansuse.playwithkotlin.view.searchmatch.SearchableActivity
import com.dansuse.playwithkotlin.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MatchesFragment : Fragment(), AnkoComponent<Context>, MatchesView {

  companion object {
    const val KEY_IS_PREV_MATCH_MODE = "key_is_prev_match_mode"
    var presenter: MatchesPresenter? = null
  }

  private lateinit var listTeam: RecyclerView
  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var spinner: Spinner
  private lateinit var textViewErrorMessage: TextView

  private var events: MutableList<Event> = mutableListOf()
  private lateinit var presenter: MatchesPresenter
  private lateinit var adapter: MainAdapter
  private var leagueId: String? = null
  private var isPrevMatchMode: Boolean = true

  private val idlingRes: CountingIdlingResource = CountingIdlingResource("MatchesFragment")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.getBoolean(MatchesFragment.KEY_IS_PREV_MATCH_MODE)?.let {
      isPrevMatchMode = it
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    swipeRefresh.setOnRefreshListener {
      if (leagueId == null) {
        presenter.getLeagueList()
      } else {
        presenter.get15EventsByLeagueId(leagueId ?: "", isPrevMatchMode)
      }
    }

    adapter = MainAdapter(isPrevMatchMode, events) {
      context?.startActivity<MatchDetailActivity>("event" to it.id)
    }
    listTeam.adapter = adapter

    initPresenter()
    presenter.getLeagueList()

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Log.d("tes123", "on item selected masuk")
        leagueId = (spinner.selectedItem as League).id
        if (isPrevMatchMode) {
          presenter.get15EventsByLeagueId(leagueId ?: "", isPrevMatchMode)
        } else {
          presenter.get15EventsByLeagueId(leagueId ?: "", isPrevMatchMode)
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
  }

  private fun initPresenter() {
    Companion.presenter?.let { this.presenter = it }
    if (this::presenter.isInitialized) {
      return
    }
    presenter = MatchesPresenter(this, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    setHasOptionsMenu(true)
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui) {
    linearLayout {
      lparams(width = matchParent, height = matchParent)
      orientation = LinearLayout.VERTICAL
      topPadding = dip(16)

      spinner = spinner {
        id = R.id.spinner_league
      }.lparams {
        leftPadding = dip(16)
        rightPadding = dip(16)
      }
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
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater?.inflate(R.menu.master_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when(item?.itemId){
      R.id.action_search -> {
        context?.startActivity<SearchableActivity>()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  }

  override fun showLoading() {
    swipeRefresh.isRefreshing = true
  }

  override fun hideLoading() {
    swipeRefresh.isRefreshing = false
  }

  override fun showLeagueList(data: List<League>) {
    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, data)
    spinner.adapter = spinnerAdapter
  }

  override fun showEventList(data: List<Event>) {
    //Log.d("tes123", "Terpanggil")
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

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }

  fun getIdlingResourceInTest(): CountingIdlingResource {
    return idlingRes
  }
}