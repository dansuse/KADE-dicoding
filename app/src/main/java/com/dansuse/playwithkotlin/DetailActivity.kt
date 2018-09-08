package com.dansuse.playwithkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*

class DetailActivity : AppCompatActivity() {
  companion object {
    val PARCELABLE_EXTRA_ITEM = "item"
  }
  private lateinit var clubNameTextView: TextView
  private lateinit var clubImageTextView: ImageView
  private lateinit var clubDescriptionTextView: TextView
  private lateinit var item:Item
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    verticalLayout {
      padding = dip(16)
      gravity = Gravity.CENTER_HORIZONTAL
      clubImageTextView = imageView().lparams(width = dip(100), height = dip(100))
      clubNameTextView = textView(){
        gravity = Gravity.CENTER
        textSize = 18f
      }.lparams(){
        topMargin = dip(4)
      }
      clubDescriptionTextView = textView(){
        textSize = 18f
      }.lparams(){
        topMargin = dip(8)
      }
    }
    val intent = intent
    item = intent.getParcelableExtra(PARCELABLE_EXTRA_ITEM)
    clubNameTextView.text = item.name
    Glide.with(this).load(item.image).into(clubImageTextView)
    clubDescriptionTextView.text = item.description
  }
}
