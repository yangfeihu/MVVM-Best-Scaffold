package com.xiaojianjun.wanandroid.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xiaojianjun.wanandroid.common.dialog.ProgressDialogFragment
import java.lang.Exception

abstract class CommonActivity : AppCompatActivity() {


    private lateinit var progressDialogFragment: ProgressDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!initDataViewBinding()){
            setContentView(layoutRes())
        }

    }




    //布局资源，子类重载这个方法
    open fun layoutRes() = 0


    //子类重写了这个类时，返回true
    open fun initDataViewBinding(): Boolean {
        return false;
    }

    /**
     * 显示加载(转圈)对话框
     */
    fun showProgressDialog(@StringRes message: Int) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(supportFragmentManager, message, false)
        }
    }

    /**
     * 隐藏加载(转圈)对话框
     */
    fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }

}
