package com.dansuse.playwithkotlin.view.teamdetail.overview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.presenter.teamdetail.TeamOverviewPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.nestedScrollView

class TeamOverviewFragment : Fragment(), AnkoComponent<Context>, TeamOverviewView {

  companion object {
    const val EXTRA_TEAM_ID = "extra_team_overview_fragment_team_id"
  }

  private lateinit var presenter:TeamOverviewPresenter
  private lateinit var teamId:String

  private lateinit var progressBar: ProgressBar
  private lateinit var textErrorMessage: TextView
  private lateinit var teamDescription: TextView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.getString(EXTRA_TEAM_ID)?.let {
      teamId = it
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    presenter = TeamOverviewPresenter(this, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
    presenter.getTeamDetailByTeamId(teamId)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui){
    nestedScrollView{
      lparams(width = matchParent, height = wrapContent)

      frameLayout {

        teamDescription = textView().lparams{
          setMargins(dip(12), dip(12), dip(12), dip(12))
        }
        textErrorMessage = textView().lparams{
          gravity = Gravity.CENTER
          setMargins(dip(12), dip(12), dip(12), dip(12))
        }
        progressBar = progressBar()
      }.lparams(width = matchParent, height = wrapContent)
    }
  }

  override fun showLoading() {
    progressBar.visible()
  }

  override fun hideLoading() {
    progressBar.invisible()
  }

  override fun showTeamDescription(data: String) {
    textErrorMessage.invisible()
    teamDescription.visible()
    teamDescription.text = data
  }

  override fun showErrorMessage(errorMessage: String) {
    teamDescription.invisible()
    textErrorMessage.visible()
    textErrorMessage.text = errorMessage
  }
}