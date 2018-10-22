package com.dansuse.playwithkotlin

import android.view.View

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.disable(){
  isEnabled = false
}

fun View.enable(){
  isEnabled = true
}
