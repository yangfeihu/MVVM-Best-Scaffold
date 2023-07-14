package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by yangfeihu on 2019-11-15.
 */
@Keep
data class Navigation(
    val cid: Int,
    val name: String,
    val articles: MutableList<Article>
)