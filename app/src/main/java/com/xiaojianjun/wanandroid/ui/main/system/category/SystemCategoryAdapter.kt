package com.xiaojianjun.wanandroid.ui.main.system.category

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Category
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * Created by yangfeihu on 2019-11-17.
 */
class SystemCategoryAdapter(
    layoutResId: Int = R.layout.item_system_category,
    categoryList: MutableList<Category>,
    var checked: Pair<Int, Int>
) : BaseQuickAdapter<Category, BaseViewHolder>(layoutResId, categoryList) {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {
            val title = findViewById<TextView>(R.id.title)
            val tagFlowLayout = findViewById<TagFlowLayout>(R.id.tagFlowLayout)
            title.text = item.name.htmlToSpanned()
            tagFlowLayout.adapter = ItemTagAdapter(item.children)
            if (checked.first == holder.adapterPosition) {
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { _, position, _ ->
                checked = holder.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({
                    onCheckedListener?.invoke(checked)
                }, 300)
                true
            }
        }
    }
}