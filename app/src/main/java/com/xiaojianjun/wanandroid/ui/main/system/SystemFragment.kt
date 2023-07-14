package com.xiaojianjun.wanandroid.ui.main.system

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.adapter.SimpleFragmentPagerAdapter
import com.xiaojianjun.wanandroid.databinding.FragmentSystemBinding
import com.xiaojianjun.wanandroid.ext.toArrayList
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import com.xiaojianjun.wanandroid.ui.main.system.category.SystemCategoryFragment
import com.xiaojianjun.wanandroid.ui.main.system.pager.SystemPagerFragment


/**
 * Created by yangfeihu on 2019-11-11.
 */
class SystemFragment : BaseFragment<FragmentSystemBinding,SystemViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private var currentOffset = 0
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<SystemPagerFragment>()
    private var categoryFragment: SystemCategoryFragment? = null

    override fun layoutRes() = R.layout.fragment_system

    override fun viewModelClass() = SystemViewModel::class.java

    override fun initView() {
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getArticleCategory()
        }
        mBinding.ivFilter.setOnClickListener {
            categoryFragment?.show(childFragmentManager)
        }
        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            categories.observe(viewLifecycleOwner) { categories ->
                mBinding.ivFilter.visibility = View.VISIBLE
                mBinding.tabLayout.visibility = View.VISIBLE
                mBinding.viewPager.visibility = View.VISIBLE
                val newCategories = categories.filter {
                    it.children.isNotEmpty()
                }.toMutableList()
                setup(newCategories)
                categoryFragment = SystemCategoryFragment.newInstance(newCategories.toArrayList())
            }
            loadingStatus.observe(viewLifecycleOwner) {
                mBinding.progressBar.isVisible = it
            }
            reloadStatus.observe(viewLifecycleOwner) {
                mBinding.reloadView.root.isVisible = it
            }
        }
    }

    override fun initData() {
        mViewModel.getArticleCategory()
    }

    private fun setup(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(SystemPagerFragment.newInstance(it.children.toArrayList()))
        }
        mBinding.viewPager.adapter =
            SimpleFragmentPagerAdapter(
                childFragmentManager,
                fragments,
                titles
            )
        mBinding.viewPager.offscreenPageLimit = titles.size
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    override fun scrollToTop() {
        if (fragments.isEmpty() ||  mBinding.viewPager == null) return
        fragments[mBinding.viewPager.currentItem].scrollToTop()
    }

    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty() ||  mBinding.viewPager == null) return 0 to 0
        val first =  mBinding.viewPager.currentItem
        val second = fragments[ mBinding.viewPager.currentItem].checkedPosition
        return first to second
    }

    fun check(position: Pair<Int, Int>) {
        if (fragments.isEmpty() ||  mBinding.viewPager == null) return
        mBinding.viewPager.currentItem = position.first
        fragments[position.first].check(position.second)
    }

}