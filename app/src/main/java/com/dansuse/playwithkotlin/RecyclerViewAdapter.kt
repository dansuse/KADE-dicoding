package com.dansuse.playwithkotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list.view.*

class RecyclerViewAdapter(private val context: Context, private val items:List<Item>,
                          private val onItemClickListener: OnItemClick)
  : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
      = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindItem(items[position])
  }

  override fun getItemCount() : Int = items.size

  inner class ViewHolder(containerView: View): RecyclerView.ViewHolder(containerView), View.OnClickListener {
    private lateinit var item:Item
    fun bindItem(item:Item){
      this.item = item
      itemView.setOnClickListener(this)
      itemView.name.text = item.name
      Glide.with(itemView.context).load(item.image).into(itemView.image)
    }
    override fun onClick(v: View?) {
      onItemClickListener.onClick(item)
    }
  }
}

