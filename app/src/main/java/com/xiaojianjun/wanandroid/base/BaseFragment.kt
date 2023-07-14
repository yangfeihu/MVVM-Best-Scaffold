package com.xiaojianjun.wanandroid.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseFragment<V : ViewDataBinding,VM : BaseViewModel> : BaseVmFragment<VM>() {

    lateinit var mBinding: V
    override fun initDataViewBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        mBinding = DataBindingUtil.inflate(inflater,layoutRes(), container, false)
        return mBinding.root
    }

}
