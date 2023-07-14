package com.xiaojianjun.wanandroid.ui.main.navigation

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.FragmentNavigationBinding
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.MainActivity



class NavigationFragment : BaseFragment<FragmentNavigationBinding,NavigationViewModel>(), ScrollToTop {

    private lateinit var mAdapter: NavigationAdapter
    private var currentPosition = 0

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun layoutRes() = R.layout.fragment_navigation

    override fun viewModelClass() = NavigationViewModel::class.java

    override fun initView() {

        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getNavigations() }
        }
        mAdapter = NavigationAdapter(R.layout.item_navigation).also {
            it.onItemTagClickListener = { article ->
                ActivityHelper.startActivity(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            mBinding.recyclerView.adapter = it
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getNavigations()
        }
        mBinding.recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
            val lm =  mBinding.recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                mBinding.tvFloatTitle.y = if (nextView.top <  mBinding.tvFloatTitle.measuredHeight) {
                    (nextView.top -  mBinding.tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            navigations.observe(viewLifecycleOwner) {
                mBinding.tvFloatTitle.isGone = it.isEmpty()
                mBinding.tvFloatTitle.text = it[0].name
                mAdapter.setList(it)
            }
            refreshStatus.observe(viewLifecycleOwner) {
                mBinding.swipeRefreshLayout.isRefreshing = it
            }
            reloadStatus.observe(viewLifecycleOwner) {
                mBinding.reloadView.root.isVisible = it
            }
        }
    }

    override fun initData() {
        mViewModel.getNavigations()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}
