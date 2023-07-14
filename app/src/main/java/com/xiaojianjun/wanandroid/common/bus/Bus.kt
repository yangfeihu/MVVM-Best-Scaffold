package com.xiaojianjun.wanandroid.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cody.bus.ElegantBus
import cody.bus.ObserverWrapper


/**
 * Created by yangfeihu on 2019-11-25.
 */
object Bus {

    /**
     * 发布LiveDataEventBus消息
     */
    inline fun <reified T : Any> post(channel: String, value: T) {
        ElegantBus.getDefault(channel).post(value)
    }

    /**
     * 订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param owner 生命周期owner
     * @param observer 观察者
     */
    inline fun <reified T : Any> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        ElegantBus.getDefault(channel).observe(owner, object : ObserverWrapper<Any>() {
            override fun onChanged(value: Any?) {
                observer.onChanged(value as T)
            }
        })
    }

    /**
     * 应用进程生命周期内订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param observer 观察者
     */
    inline fun <reified T> observeForever(channel: String, observer: Observer<T>) {
        ElegantBus.getDefault(channel).observeForever(object : ObserverWrapper<Any>() {
            override fun onChanged(value: Any?) {
                observer.onChanged(value as T)
            }
        })
    }

    /**
     * 订阅粘性LiveDataEventBus消息
     * @param channel 渠道
     * @param owner 生命周期owner
     * @param observer 观察者
     */
    inline fun <reified T> observeSticky(
        channel: String,
        owner: LifecycleOwner,
        observer: Observer<T>
    ) {
        ElegantBus.getDefault(channel).observeSticky(owner,object : ObserverWrapper<Any>() {
            override fun onChanged(value: Any?) {
                observer.onChanged(value as T)
            }
        })
    }

}


