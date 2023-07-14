package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by yangfeihu on 2019-12-02.
 */
@Keep
data class PointRank(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)