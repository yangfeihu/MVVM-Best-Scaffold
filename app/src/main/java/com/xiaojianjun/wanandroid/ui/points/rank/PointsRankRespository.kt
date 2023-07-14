package com.xiaojianjun.wanandroid.ui.points.rank

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by yangfeihu on 2019-12-02.
 */
class PointsRankRespository {
    suspend fun getPointsRank(page: Int) =
        RetrofitClient.apiService.getPointsRank(page).apiData()
}