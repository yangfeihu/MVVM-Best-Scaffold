package com.xiaojianjun.wanandroid.ui.main.home.wechat

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by yangfeihu on 2019-09-18.
 */
class WechatRepository {
    suspend fun getWechatCategories() = RetrofitClient.apiService.getWechatCategories().apiData()
    suspend fun getWechatArticleList(page: Int, id: Int) =
        RetrofitClient.apiService.getWechatArticleList(page, id).apiData()
}