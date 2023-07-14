package com.xiaojianjun.wanandroid.ui.share

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by yangfeihu on 2019-12-01.
 */
class ShareRepository {
    suspend fun shareArticle(title: String, link: String) =
        RetrofitClient.apiService.shareArticle(title, link).apiData()
}