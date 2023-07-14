package com.xiaojianjun.wanandroid.ui.main.discovery

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.HotWord

/**
 * Created by yangfeihu on 2019-11-16.
 */
class HotWordsAdapter(layouResId: Int = R.layout.item_hot_word) :
    BaseQuickAdapter<HotWord, BaseViewHolder>(layouResId) {
    override fun convert(holder: BaseViewHolder, item: HotWord) {
        holder.itemView.findViewById<TextView>(R.id.tvName).text = item.name;
    }


}