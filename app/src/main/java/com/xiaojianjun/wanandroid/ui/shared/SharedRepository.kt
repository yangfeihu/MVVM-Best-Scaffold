package com.xiaojianjun.wanandroid.ui.shared

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by yangfeihu on 2019-12-03.
 */
class SharedRepository {
    suspend fun getSharedArticleList(page: Int) =
        RetrofitClient.apiService.getSharedArticleList(page).apiData()

    suspend fun deleteShared(id: Long) = RetrofitClient.apiService.deleteShare(id).apiData()
}