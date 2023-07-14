package com.xiaojianjun.wanandroid.ui.main.navigation

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Navigation
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * Created by yangfeihu on 2019-11-15.
 */
class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Navigation) {
        holder.itemView.run {

            val title = findViewById<TextView>(R.id.title)
            val tagFlawLayout = findViewById<TagFlowLayout>(R.id.tagFlawLayout)

            title.text = item.name
            tagFlawLayout.adapter = ItemTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }
    }
}