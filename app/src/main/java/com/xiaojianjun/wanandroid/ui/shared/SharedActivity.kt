package com.xiaojianjun.wanandroid.ui.shared

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.ActivitySharedBinding
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import com.xiaojianjun.wanandroid.ui.share.ShareActivity


class SharedActivity : BaseActivity<ActivitySharedBinding,SharedViewModel>() {

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.activity_shared

    override fun viewModelClass() = SharedViewModel::class.java

    override fun initView() {
        initAdapter()
        initRefresh()
        initListeners()
    }

    private fun initAdapter() {
        mAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
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
            it.setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@SharedActivity)
                    .setMessage(R.string.confirm_delete_shared)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.deleteShared(it.data[position].id)
                        it.removeAt(position)
                        mBinding.emptyView.root.isVisible = it.data.isEmpty()
                    }.show()
                true
            }
            mBinding.recyclerView.adapter = it
        }
    }

    private fun initRefresh() {
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }
    }

    private fun initListeners() {


        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
        mBinding.ivAdd.setOnClickListener {
            ActivityHelper.startActivity(ShareActivity::class.java)
        }
        mBinding.ivBack.setOnClickListener {
            ActivityHelper.finish(SharedActivity::class.java)
        }
    }

    override fun initData() {
        mViewModel.refreshArticleList()
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(this@SharedActivity) {
            mAdapter.setList(it)
        }
        mViewModel.refreshStatus.observe(this@SharedActivity) {
            mBinding.swipeRefreshLayout.isRefreshing = it
        }
        mViewModel.emptyStatus.observe(this@SharedActivity) {
            mBinding.emptyView.root.isVisible = it
        }
        mViewModel.loadMoreStatus.observe(this@SharedActivity) {
            mAdapter.loadMoreModule.setLoadMoreStatus(it)
        }
        mViewModel.reloadStatus.observe(this@SharedActivity) {
            mBinding.reloadView.root.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }
}
