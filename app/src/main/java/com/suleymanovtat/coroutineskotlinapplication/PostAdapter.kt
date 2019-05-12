package com.suleymanovtat.coroutineskotlinapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_posts.view.*

class PostAdapter(items: MutableList<Posts>,
                  private val listener: MainActivity)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var items: MutableList<Posts> = items
        set(items) {
            field = items
            this.notifyDataSetChanged();
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_posts, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.mView) {
            tvTitle.text = item.title
            tvBody.text = item.body
            setOnClickListener {
                listener?.onNoteClick(item)
            }
        }
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    override fun getItemCount(): Int = items.size

    interface OnNoteClickListener {
        fun onNoteClick(item: Posts)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
}