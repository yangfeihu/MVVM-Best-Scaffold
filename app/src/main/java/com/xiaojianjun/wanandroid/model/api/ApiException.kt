package com.xiaojianjun.wanandroid.model.api

//定义异常类
class ApiException(var code: Int, override var message: String) : RuntimeException()