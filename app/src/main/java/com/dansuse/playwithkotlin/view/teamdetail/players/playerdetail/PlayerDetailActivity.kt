package com.dansuse.playwithkotlin.view.teamdetail.players.playerdetail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import com.dansuse.playwithkotlin.invisible
import com.dansuse.playwithkotlin.model.Player
import com.dansuse.playwithkotlin.presenter.teamdetail.PlayerDetailPresenter
import com.dansuse.playwithkotlin.repository.TheSportDBApiService
import com.dansuse.playwithkotlin.visible
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*

class PlayerDetailActivity : AppCompatActivity(), PlayerDetailView {

  private lateinit var presenter:PlayerDetailPresenter

  private lateinit var parentView: ScrollView
  private lateinit var progressBar:ProgressBar
  private lateinit var textViewErrorMessage:TextView
  private lateinit var playerThumbnail: ImageView
  private lateinit var playerHeight:TextView
  private lateinit var playerWeight:TextView
  private lateinit var playerPosition:TextView
  private lateinit var playerDescription:TextView

  private lateinit var playerId: String
  private lateinit var player:Player


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    frameLayout {
      lparams(width = matchParent, height = matchParent)

      parentView = scrollView {
        linearLayout {

          orientation = LinearLayout.VERTICAL

          playerThumbnail = imageView()
          linearLayout {
            orientation = LinearLayout.HORIZONTAL

            linearLayout {
              orientation = LinearLayout.VERTICAL
              textView("Weight (Kg)").lparams{
                gravity = Gravity.CENTER_HORIZONTAL
              }
              playerWeight = textView("65.76"){
                textSize = 30.0f
                gravity = Gravity.CENTER
              }.lparams{
                gravity = Gravity.CENTER_HORIZONTAL
              }
            }.lparams(width = dip(0), height = wrapContent){
              weight = 1.0f
            }
            linearLayout {
              orientation = LinearLayout.VERTICAL
              textView("Height (m)").lparams{
                gravity = Gravity.CENTER_HORIZONTAL
              }
              playerHeight = textView("1.68"){
                textSize = 30.0f
                gravity = Gravity.CENTER
              }.lparams{
                gravity = Gravity.CENTER_HORIZONTAL
              }
            }.lparams(width = dip(0), height = wrapContent){
              weight = 1.0f
            }
          }
          playerPosition = textView(){
            textSize = 20.0f
          }.lparams{
            gravity = Gravity.CENTER_HORIZONTAL
          }
          view {
            background = ColorDrawable(Color.LTGRAY)
          }.lparams(
              width = matchParent, height = dip(4)
          )
          playerDescription = textView().lparams(){
            setMargins(dip(12), dip(12), dip(12), dip(12))
          }
        }.lparams(width = matchParent, height = matchParent)
      }.lparams(width = matchParent, height = matchParent)

      textViewErrorMessage = textView(){

      }.lparams(){
        gravity = Gravity.CENTER
      }
      progressBar = progressBar()
    }

    val intent = intent
    playerId = intent.getStringExtra("id")

    presenter = PlayerDetailPresenter(this, TheSportDBApiService.create(),
        Schedulers.io(), AndroidSchedulers.mainThread())
    presenter.getPlayerDetailByPlayerId(playerId)
  }

  override fun showLoading() {
    progressBar.visible()
  }

  override fun hideLoading() {
    progressBar.invisible()
  }

  override fun showPlayerDetail(data: Player) {
    textViewErrorMessage.invisible()
    parentView.visible()
    player = data
    supportActionBar?.title = data.playerName
    playerHeight.text = player.height ?: "-"
    playerWeight.text = player.weight ?: "-"
    playerPosition.text = player.position ?: "-"
    playerDescription.text = player.description ?: "No Description Available"
    Picasso.get().load(player.urlThumbnail).into(playerThumbnail)
  }

  override fun showErrorMessage(errorMessage: String) {
    parentView.invisible()
    textViewErrorMessage.visible()
    textViewErrorMessage.text = errorMessage
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }
}
