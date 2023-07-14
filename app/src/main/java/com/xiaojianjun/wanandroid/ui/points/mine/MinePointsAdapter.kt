package com.xiaojianjun.wanandroid.ui.points.mine

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.ext.toDateTime
import com.xiaojianjun.wanandroid.model.bean.PointRecord

/**
 * Created by yangfeihu on 2019-12-02.
 */
@SuppressLint("SetTextI18n")
class MinePointsAdapter :
    BaseLoadMoreAdapter<PointRecord, BaseViewHolder>(R.layout.item_mine_points) {
    override fun convert(holder: BaseViewHolder, item: PointRecord) {
        holder.itemView.run {
            val tvReason = findViewById<TextView>(R.id.tvReason)
            val tvTime = findViewById<TextView>(R.id.tvTime)
            val tvPoint = findViewById<TextView>(R.id.tvPoint)
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("YYYY-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }

}