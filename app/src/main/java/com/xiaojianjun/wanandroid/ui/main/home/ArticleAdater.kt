package com.xiaojianjun.wanandroid.ui.main.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Article

/**
 * Created by yangfeihu on 2019-11-06.
 */
class ArticleAdapter(layoutResId: Int = R.layout.item_article) :
    BaseLoadMoreAdapter<Article, BaseViewHolder>(layoutResId) {

    init {
        addChildClickViewIds(R.id.iv_collect)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            itemView.run {

                this.findViewById<TextView>(R.id.tv_author).text  = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }

                this.findViewById<TextView>(R.id.tv_top).isVisible = item.top
                this.findViewById<TextView>(R.id.tv_fresh).isVisible =item.fresh && !item.top

                val tv_tag =   this.findViewById<TextView>(R.id.tv_tag)
                tv_tag.visibility = if (item.tags.isNotEmpty()) {
                    tv_tag.text = item.tags[0].name
                    View.VISIBLE
                } else {
                    View.GONE
                }

                val tv_chapter =   this.findViewById<TextView>(R.id.tv_chapter)
                tv_chapter.text = when {
                    !item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                        "${item.superChapterName.htmlToSpanned()}/${item.chapterName.htmlToSpanned()}"
                    item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                        item.chapterName.htmlToSpanned()
                    !item.superChapterName.isNullOrEmpty() && item.chapterName.isNullOrEmpty() ->
                        item.superChapterName.htmlToSpanned()
                    else -> ""

                }
                val tv_title =   this.findViewById<TextView>(R.id.tv_title)
                tv_title.text = item.title.htmlToSpanned()
                val tv_desc =   this.findViewById<TextView>(R.id.tv_desc)
                tv_desc.text = item.desc.htmlToSpanned()
                tv_desc.isGone = item.desc.isNullOrEmpty()
                val tv_time =   this.findViewById<TextView>(R.id.tv_time)
                tv_time.text = item.niceDate
                val iv_collect =   this.findViewById<ImageView>(R.id.iv_collect)
                iv_collect.isSelected = item.collect
            }
        }
    }
}