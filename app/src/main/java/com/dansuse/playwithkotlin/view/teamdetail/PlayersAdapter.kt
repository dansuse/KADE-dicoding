package com.dansuse.playwithkotlin.view.teamdetail

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dansuse.playwithkotlin.R
import com.dansuse.playwithkotlin.model.Player
import com.dansuse.playwithkotlin.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class PlayersAdapter(private val players: List<Player>, private val listener: (Player) -> Unit) : RecyclerView.Adapter<PlayerViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
    return PlayerViewHolder(PlayerUI().createView(AnkoContext.create(parent.context, parent)))
  }

  override fun getItemCount(): Int = players.size

  override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
    holder.bindItem(players[position], listener)
  }
}

class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  private val playerPhoto: ImageView = view.find(R.id.player_photo)
  private val playerName: TextView = view.find(R.id.player_name)
  private val playerPosition: TextView = view.find(R.id.player_position)

  fun bindItem(player: Player, listener: (Player) -> Unit) {
    Picasso.get().load(player.urlCutout).into(playerPhoto)
    playerName.text = player.playerName
    playerPosition.text = player.position
    itemView.onClick { listener(player) }
  }
}

class PlayerUI : AnkoComponent<ViewGroup> {
  override fun createView(ui: AnkoContext<ViewGroup>): View {
    return with(ui) {
      frameLayout {
        lparams(width = matchParent, height = wrapContent)
        linearLayout {
          padding = dip(16)
          orientation = LinearLayout.HORIZONTAL

          imageView {
            id = R.id.player_photo
          }.lparams {
            height = dip(50)
            width = dip(50)
          }
          textView {
            id = R.id.player_name
            textSize = 16f
          }.lparams {
            margin = dip(15)
          }
        }.lparams(width = matchParent, height = wrapContent){
          gravity = Gravity.CENTER_VERTICAL or Gravity.START
        }
        textView {
          id = R.id.player_position
          textSize = 16f
        }.lparams {
          margin = dip(15)
          gravity = Gravity.CENTER_VERTICAL or Gravity.END
        }
      }
    }
  }
}