package com.xiaojianjun.wanandroid.ui.points.rank

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.model.bean.PointRank

/**
 * Created by yangfeihu on 2019-12-02.
 */
@SuppressLint("SetTextI18n")
class PointsRankAdapter :
    BaseLoadMoreAdapter<PointRank, BaseViewHolder>(R.layout.item_points_rank) {
    override fun convert(holder: BaseViewHolder, item: PointRank) {
        holder.itemView.run {
            this.findViewById<TextView>(R.id.tvNo).text = "${holder.adapterPosition + 1}"
            this.findViewById<TextView>(R.id.tvName).text   = item.username
            this.findViewById<TextView>(R.id.tvPoints).text = item.coinCount.toString()
        }
    }
}