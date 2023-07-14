package com.xiaojianjun.wanandroid.ui.main.discovery

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Frequently
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class TagAdapter(private val frequentlyList: List<Frequently>) :
    TagAdapter<Frequently>(frequentlyList) {
    override fun getView(parent: FlowLayout?, position: Int, frequently: Frequently?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                this.findViewById<TextView>(R.id.tvTag).text = frequentlyList[position].name;
            }
    }
}