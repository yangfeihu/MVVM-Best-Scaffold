package com.xiaojianjun.wanandroid.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseVmActivity<VM>() {

    lateinit var mBinding: V
    override fun initDataViewBinding(): Boolean {
        mBinding = DataBindingUtil.setContentView(this, layoutRes())
        //binding.setVariable(BR.viewModel, mViewModel)
        mBinding.lifecycleOwner = this
        return true;
    }

}
