package com.xiaojianjun.wanandroid.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.CommonActivity
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.databinding.ActivityMainBinding
import com.xiaojianjun.wanandroid.ext.showToast
import com.xiaojianjun.wanandroid.ui.main.discovery.DiscoveryFragment
import com.xiaojianjun.wanandroid.ui.main.home.HomeFragment
import com.xiaojianjun.wanandroid.ui.main.navigation.NavigationFragment
import com.xiaojianjun.wanandroid.ui.main.profile.ProfileFragment
import com.xiaojianjun.wanandroid.ui.main.system.SystemFragment


class MainActivity : CommonActivity() {

    private lateinit var fragments: Map<Int, Fragment>
    private var bottomNavigationViewAnimtor: ViewPropertyAnimator? = null
    private var currentBottomNavagtionState = true
    private var previousTimeMillis = 0L

    private lateinit var viewBinding: ActivityMainBinding


    override fun initDataViewBinding(): Boolean {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //将Fragment保存到map中
        fragments = mapOf(
            R.id.home to createFragment(HomeFragment::class.java),
            R.id.system to createFragment(SystemFragment::class.java),
            R.id.discovery to createFragment(DiscoveryFragment::class.java),
            R.id.navigation to createFragment(NavigationFragment::class.java),
            R.id.mine to createFragment(ProfileFragment::class.java)
        )

         //监听设置
        viewBinding.bottomNavigationView.run {
             setOnNavigationItemSelectedListener { menuItem ->
                 showFragment(menuItem.itemId)
                 true
             }
             setOnNavigationItemReselectedListener { menuItem ->
                 val fragment = fragments[menuItem.itemId]
                 if (fragment is ScrollToTop) {
                     fragment.scrollToTop()
                 }
             }
         }

        //设置默认的Fragment
        if (savedInstanceState == null) {
            val initialItemId = R.id.home
            viewBinding.bottomNavigationView.selectedItemId = initialItemId
            showFragment(initialItemId)
        }
    }

    //创建一个Fragment
    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { it.javaClass == clazz }
        if (fragment == null) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment.newInstance()
                SystemFragment::class.java -> SystemFragment.newInstance()
                DiscoveryFragment::class.java -> DiscoveryFragment.newInstance()
                NavigationFragment::class.java -> NavigationFragment.newInstance()
                ProfileFragment::class.java -> ProfileFragment.newInstance()
                else -> throw IllegalArgumentException("argument ${clazz.simpleName} is illegal")
            }
        }
        return fragment
    }

    //显示Fragment
    private fun showFragment(menuItemId: Int) {
        //查找当前的Fragment
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in fragments.values
        }
        val targetFragment = fragments[menuItemId]
        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { if (it.isVisible) hide(it) }
            targetFragment?.let {
                if (it.isAdded) show(it) else add(R.id.fl, it)
            }
        }.commit()
    }

    //切换动画
    @SuppressLint("RestrictedApi")
    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavagtionState == show) {
            return
        }
        if (bottomNavigationViewAnimtor != null) {
            bottomNavigationViewAnimtor?.cancel()
            viewBinding.bottomNavigationView.clearAnimation()
        }
        currentBottomNavagtionState = show
        val targetY = if (show) 0F else viewBinding.bottomNavigationView.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L
        bottomNavigationViewAnimtor = viewBinding.bottomNavigationView.animate()
            .translationY(targetY)
            .setDuration(duration)
            .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
            .setListener(object : AnimatorListenerAdapter() {
                override  fun onAnimationEnd(animation: Animator) {
                    bottomNavigationViewAnimtor = null
                }
            })
    }

     //返回按键处理
    override fun onBackPressed() {
        val currentTimMillis = System.currentTimeMillis()
        if (currentTimMillis - previousTimeMillis < 2000) {
            super.onBackPressed()
        } else {
            showToast(R.string.press_again_to_exit)
            previousTimeMillis = currentTimMillis
        }
    }

    //activity销毁
    override fun onDestroy() {
        bottomNavigationViewAnimtor?.cancel()
        viewBinding.bottomNavigationView.clearAnimation()
        bottomNavigationViewAnimtor = null
        super.onDestroy()
    }
}

/**
 * 首页：热门文章、最新项目、广场、项目分类、公众号
 * 体系：体系
 * 发现：Banner、搜索热词、常用网站
 * 导航：导航
 * 我的：登录、注册、积分排行、我的积分、我的分享、稍后阅读、我的收藏、浏览历史、关于作者、开源项目、系统设置
 * 详情：文章详情（收藏、分享、浏览器打开、复制链接、刷新页面）
 * 搜索：搜索历史、热门搜索
 * 设置：日夜间模式、调整亮度、字体大小、清除缓存、检查版本、关于玩安卓、退出登录
 */
