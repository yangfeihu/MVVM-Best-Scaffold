package com.xiaojianjun.wanandroid.ui.main.home.plaza

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.FragmentPlazaBinding
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.SimpleArticleAdapter


class PlazaFragment : BaseFragment<FragmentPlazaBinding,PlazaViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = PlazaFragment()
    }

    private lateinit var mAdapter: SimpleArticleAdapter

    override fun layoutRes() = R.layout.fragment_plaza

    override fun viewModelClass() = PlazaViewModel::class.java

    override fun initView() {
        initRefresh()
        initAdapter()
        initListeners()
    }

    private fun initRefresh() {
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }
    }

    private fun initAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.uncollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            mBinding.recyclerView.adapter = it
        }
    }

    private fun initListeners() {
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(viewLifecycleOwner) {
            mAdapter.setList(it)
        }
        mViewModel.refreshStatus.observe(viewLifecycleOwner) {
            mBinding.swipeRefreshLayout.isRefreshing = it
        }
        mViewModel.loadMoreStatus.observe(viewLifecycleOwner) {
            mAdapter.loadMoreModule.setLoadMoreStatus(it)
        }
        mViewModel.reloadStatus.observe(viewLifecycleOwner) {
            mBinding.reloadView.root.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

}
