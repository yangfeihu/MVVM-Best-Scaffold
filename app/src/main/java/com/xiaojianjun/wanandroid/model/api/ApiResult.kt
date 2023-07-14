package com.xiaojianjun.wanandroid.model.api

import androidx.annotation.Keep

/**
 *
 * Created by yangfeihu on 2019-09-18.
 * 网络通讯协议定义
 */
@Keep
data class ApiResult<T>(val errorCode:Int, val errorMsg: String,private val data: T?) {
    fun apiData(): T {
        if (errorCode == 0 && data != null) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}
