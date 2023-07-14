package com.xiaojianjun.wanandroid

import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.core.DayNightHelper
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreHelper
import com.xiaojianjun.wanandroid.util.isMainProcess

/**
 * Created by yangfeihu on 2019-07-15.
 */
class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 主进程初始化
        if (isMainProcess(this)) {
            init()
        }
    }

    private fun init() {
        LoadMoreHelper.init()
        ActivityHelper.init(this)
        DayNightHelper.init()
    }

}