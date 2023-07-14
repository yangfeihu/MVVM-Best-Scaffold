package com.xiaojianjun.wanandroid.ui.main.home

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Article

/**
 * Created by yangfeihu on 2019-11-06.
 */
class SimpleArticleAdapter(layoutResId: Int = R.layout.item_article_simple) :
    BaseLoadMoreAdapter<Article, BaseViewHolder>(layoutResId) {

    init {
        addChildClickViewIds(R.id.iv_collect)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            itemView.run {
                val tv_author = this.findViewById<TextView>(R.id.tv_author)
                val tv_fresh = this.findViewById<TextView>(R.id.tv_fresh)
                val tv_title = this.findViewById<TextView>(R.id.tv_title)
                val tv_time = this.findViewById<TextView>(R.id.tv_time)
                val iv_collect = this.findViewById<ImageView>(R.id.iv_collect)


                tv_author.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_fresh.isVisible = item.fresh
                tv_title.text = item.title.htmlToSpanned()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
        }
    }
}