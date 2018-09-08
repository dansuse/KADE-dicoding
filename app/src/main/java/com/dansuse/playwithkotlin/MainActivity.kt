package com.dansuse.playwithkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), OnItemClick {
  private var items: MutableList<Item> = mutableListOf()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initData()
    club_list.layoutManager = LinearLayoutManager(this)
    club_list.adapter = RecyclerViewAdapter(this, items, this)
  }

  private fun initData() {
    val names = resources.getStringArray(R.array.club_name)
    val descriptions = resources.getStringArray(R.array.club_description)
    val images = resources.obtainTypedArray(R.array.club_image)
    items.clear()
    for (i in names.indices) {
      items.add(Item(names[i], images.getResourceId(i, 0), descriptions[i]))
    }
    images.recycle()
  }

  override fun onClick(item: Item) {
    startActivity<DetailActivity>(DetailActivity.PARCELABLE_EXTRA_ITEM to item)
  }

//  class MainActivityUI : AnkoComponent<MainActivity> {
//    companion object {
//      val idRecyclerView = 2
//    }
//    override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
//      frameLayout{
//        lparams(width = matchParent, height = matchParent)
//        val rv = recyclerView(){
//          id = idRecyclerView
//          lparams(width = matchParent, height = matchParent)
//        }
//      }
//    }.view
//  }
}

interface OnItemClick{
  fun onClick(item: Item)
}
