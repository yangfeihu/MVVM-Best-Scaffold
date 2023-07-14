package com.xiaojianjun.wanandroid.ui.main.system

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by yangfeihu on 2019-09-18.
 */
class SystemRepository {
    suspend fun getArticleCategories() = RetrofitClient.apiService.getArticleCategories().apiData()
}