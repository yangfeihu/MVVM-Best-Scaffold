package com.xiaojianjun.wanandroid.ui.main.navigation

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class ItemTagAdapter(private val articles: List<Article>) : TagAdapter<Article>(articles) {
    override fun getView(parent: FlowLayout?, position: Int, article: Article?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                findViewById<TextView>(R.id.tvTag).text = articles[position].title
            }
    }
}