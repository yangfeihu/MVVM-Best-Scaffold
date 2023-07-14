package com.xiaojianjun.wanandroid.ui.search.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiaojianjun.wanandroid.R


/**
 * Created by yangfeihu on 2019-11-28.
 */
class SearchHistoryAdapter(
    private var context: Context,
    private var layoutResId: Int = R.layout.item_search_history
) : ListAdapter<String, SearchHistoryHolder>(
    HistoryDiffCallback()
) {

    var onItemClickListener: ((position: Int) -> Unit)? = null
    var onDeleteClickListener: ((position: Int) -> Unit)? = null
    var data: MutableList<String> = mutableListOf()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryHolder {
        return SearchHistoryHolder(
            LayoutInflater.from(context).inflate(layoutResId, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchHistoryHolder, position: Int) {
        holder.itemView.run {
            this.findViewById<TextView>(R.id.tvLabel).text = getItem(position);


            setOnClickListener {
                onItemClickListener?.invoke(holder.adapterPosition)
            }

            this.findViewById<View>(R.id.ivDelete).setOnClickListener {
                onDeleteClickListener?.invoke(holder.adapterPosition)
            }

        }
    }

    override fun onBindViewHolder(
        holder: SearchHistoryHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun submitList(list: MutableList<String>?) {
        data = if (list.isNullOrEmpty()) {
            mutableListOf()
        } else {
            list.toMutableList()
        }
        super.submitList(data)
    }

}

class SearchHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class HistoryDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}