package com.xiaojianjun.wanandroid.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.login.LoginActivity

abstract class BaseVmActivity<VM : BaseViewModel> : CommonActivity() {

    protected open lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initView()
        initData()
        observe()
    }


    /**
     * 数据初始化相关
     */
    open fun initView() {
        // Override if need
    }

    /**
     * 懒加载数据
     */
    open fun initData() {
        // Override if need
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    /**
     * 获取ViewModel的class
     */
    protected abstract fun viewModelClass(): Class<VM>

    /**
     * 订阅，LiveData、Bus
     */
    open fun observe() {
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(this ){
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityHelper.startActivity(LoginActivity::class.java)
            }
        }
    }

    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (isLogin()) {
            then?.invoke()
            true
        } else {
            ActivityHelper.startActivity(LoginActivity::class.java)
            false
        }
    }

}
