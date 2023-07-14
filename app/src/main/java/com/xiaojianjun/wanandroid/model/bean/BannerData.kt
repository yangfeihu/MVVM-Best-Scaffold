package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by yangfeihu on 2019-11-16.
 */
@Keep
data class BannerData(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)