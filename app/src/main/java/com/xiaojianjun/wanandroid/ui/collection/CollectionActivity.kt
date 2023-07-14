package com.xiaojianjun.wanandroid.ui.collection

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.ActivityCollectionBinding
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter


class CollectionActivity : BaseActivity<ActivityCollectionBinding,CollectionViewModel>() {

    private lateinit var mAdater: ArticleAdapter

    override fun layoutRes() = R.layout.activity_collection

    override fun viewModelClass() = CollectionViewModel::class.java

    override fun initView() {
        initAdapter()
        initRefresh()
        initListeners()
    }

    private fun initAdapter() {
        mAdater = ArticleAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect) {
                    mViewModel.uncollect(article.originId)
                    removeItem(position)
                }
            }
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMore()
            }
            mBinding.recyclerView.adapter = it
        }
    }

    private fun initRefresh() {
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
    }

    private fun initListeners() {
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        mBinding.ivBack.setOnClickListener {
            ActivityHelper.finish(CollectionActivity::class.java)
        }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(this) {
            mAdater.setList(it)
        }
        mViewModel.refreshStatus.observe(this) {
            mBinding.swipeRefreshLayout.isRefreshing = it
        }
        mViewModel.emptyStatus.observe(this) {
            mBinding.emptyView.root.isVisible = it
        }
        mViewModel.loadMoreStatus.observe(this) {
            mAdater.loadMoreModule.setLoadMoreStatus(it)
        }
        mViewModel.reloadStatus.observe(this) {
            mBinding.reloadView.root.isVisible = it
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this) {
            onCollectUpdated(it.first, it.second)
        }
    }

    private fun onCollectUpdated(id: Long, collect: Boolean) {
        if (collect) {
            mViewModel.refresh()
        } else {
            val position = mAdater.data.indexOfFirst { it.originId == id }
            if (position != -1) {
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        mAdater.removeAt(position)
        mBinding.emptyView.root.isVisible = mAdater.data.isEmpty()
    }

}
