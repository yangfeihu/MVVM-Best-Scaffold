package com.xiaojianjun.wanandroid.ui.opensource

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article

/**
 * Created by yangfeihu on 2019-12-08.
 */
class OpenSourceAdapter : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_open_source) {
    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.itemView.run {
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val tvLink = findViewById<TextView>(R.id.tvLink)
            tvTitle.text = item.title
            tvLink.text = item.link
        }
    }
}