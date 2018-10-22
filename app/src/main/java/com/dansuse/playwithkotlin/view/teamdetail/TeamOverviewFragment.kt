package com.dansuse.playwithkotlin.view.teamdetail

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
import com.dansuse.playwithkotlin.visible
import org.jetbrains.anko.*

class TeamOverviewFragment : Fragment(), AnkoComponent<Context>, TeamOverviewView{

//  companion object {
//    const val EXTRA_TEAM_DESCRIPTION = "extra_team_description"
//  }

  private lateinit var progressBar: ProgressBar
  private lateinit var textErrorMessage: TextView
  private lateinit var teamDescription: TextView
  //private lateinit var stringTeamDescription:String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    arguments?.getString(TeamOverviewFragment.EXTRA_TEAM_DESCRIPTION)?.let {
//      stringTeamDescription = it
//    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    //teamDescription.text = stringTeamDescription
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createView(AnkoContext.create(requireContext()))
  }

  override fun createView(ui: AnkoContext<Context>): View = with(ui){
    frameLayout {
      lparams(width = matchParent, height = wrapContent)
      teamDescription = textView()
      textErrorMessage = textView().lparams{
        gravity = Gravity.CENTER
      }
      progressBar = progressBar()
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