package com.xiaojianjun.wanandroid.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.CommonFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.adapter.SimpleFragmentPagerAdapter
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.FragmentHomeBinding
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import com.xiaojianjun.wanandroid.ui.main.home.latest.LatestFragment
import com.xiaojianjun.wanandroid.ui.main.home.plaza.PlazaFragment
import com.xiaojianjun.wanandroid.ui.main.home.popular.PopularFragment
import com.xiaojianjun.wanandroid.ui.main.home.project.ProjectFragment
import com.xiaojianjun.wanandroid.ui.main.home.wechat.WechatFragment
import com.xiaojianjun.wanandroid.ui.search.SearchActivity


class HomeFragment : CommonFragment(), ScrollToTop {

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0
    lateinit var mBinding: FragmentHomeBinding
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun layoutRes() = R.layout.fragment_home


    override fun initDataViewBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        mBinding = DataBindingUtil.inflate(inflater,layoutRes(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragments = listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            ProjectFragment.newInstance(),
            WechatFragment.newInstance()
        )
        val titles = listOf<CharSequence>(
            getString(R.string.popular_articles),
            getString(R.string.latest_project),
            getString(
                R.string.plaza
            ),
            getString(R.string.project_category),
            getString(R.string.wechat_public)
        )
        mBinding.viewPager.adapter = SimpleFragmentPagerAdapter(
            fm = childFragmentManager,
            fragments = fragments,
            titles = titles
        )
        mBinding.viewPager.offscreenPageLimit = fragments.size
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)

        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        mBinding.llSearch.setOnClickListener { ActivityHelper.startActivity(SearchActivity::class.java) }
    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment = fragments[mBinding.viewPager.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }
}