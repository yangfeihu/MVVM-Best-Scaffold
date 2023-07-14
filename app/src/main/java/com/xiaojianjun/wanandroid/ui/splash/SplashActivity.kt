package com.xiaojianjun.wanandroid.ui.splash

import android.os.Bundle
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.CommonActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.ui.main.MainActivity

class SplashActivity : CommonActivity() {

    override fun layoutRes() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.postDelayed({
            ActivityHelper.startActivity(MainActivity::class.java)
            ActivityHelper.finish(SplashActivity::class.java)
        }, 1000)
    }
}
