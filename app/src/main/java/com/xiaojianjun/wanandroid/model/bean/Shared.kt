package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by yangfeihu on 2019-12-03.
 */
@Keep
data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)