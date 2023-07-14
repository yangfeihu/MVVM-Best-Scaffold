package com.xiaojianjun.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 */
open class CommonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initDataViewBinding(inflater,container) ?: inflater.inflate(layoutRes(), container, false)
    }


    open fun layoutRes() = 0


    open fun initDataViewBinding(inflater: LayoutInflater,container: ViewGroup?): View? {
        return null;
    }


}
