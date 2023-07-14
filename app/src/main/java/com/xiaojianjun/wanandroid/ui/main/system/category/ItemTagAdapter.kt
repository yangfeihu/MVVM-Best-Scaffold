package com.xiaojianjun.wanandroid.ui.main.system.category

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class ItemTagAdapter(private val categoryList: List<Category>) :
    TagAdapter<Category>(categoryList) {
    override fun getView(parent: FlowLayout?, position: Int, category: Category?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_system_category_tag, parent, false)
            .apply {
                val tvTag =  findViewWithTag<TextView>(R.id.tvTag)
                tvTag.text = categoryList[position].name.htmlToSpanned()
            }
    }
}