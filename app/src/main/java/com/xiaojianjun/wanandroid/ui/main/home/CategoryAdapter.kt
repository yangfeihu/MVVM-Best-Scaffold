package com.xiaojianjun.wanandroid.ui.main.home

import android.view.ViewGroup.MarginLayoutParams
import android.widget.CheckedTextView
import androidx.core.view.updateLayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.dpToPx
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Category

/**
 * Created by yangfeihu on 2019-11-14.
 */
class CategoryAdapter(layoutResId: Int = R.layout.item_category_sub) :
    BaseQuickAdapter<Category, BaseViewHolder>(layoutResId) {

    private var checkedPosition = 0
    var onCheckedListener: ((position: Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Category) {
        holder.itemView.run {

            val ctvCategory = findViewById<CheckedTextView>(R.id.ctvCategory)
            ctvCategory.text = item.name.htmlToSpanned()
            ctvCategory.isChecked = checkedPosition == holder.adapterPosition
            setOnClickListener {
                val position = holder.adapterPosition
                check(position)
                onCheckedListener?.invoke(position)
            }
            updateLayoutParams<MarginLayoutParams> {
                marginStart =
                    if (holder.adapterPosition == 0) 8.dpToPx().toInt() else 0.dpToPx().toInt()
            }
        }
    }

    fun check(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }

}