package com.xiaojianjun.wanandroid.ui.points.rank

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.ActivityPointsRankBinding


class PointsRankActivity : BaseActivity<ActivityPointsRankBinding,PointsRankViewModel>() {

    private lateinit var mAdapter: PointsRankAdapter

    override fun layoutRes() = R.layout.activity_points_rank

    override fun viewModelClass() = PointsRankViewModel::class.java

    override fun initView() {
        mAdapter = PointsRankAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreData() }
            mBinding.recyclerView.adapter = it
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshData() }
        }
        mBinding.ivBack.setOnClickListener { ActivityHelper.finish(PointsRankActivity::class.java) }
        mBinding.tvTitle.setOnClickListener { mBinding.recyclerView.smoothScrollToPosition(0) }
        mBinding.reloadView.btnReload.setOnClickListener { mViewModel.refreshData() }
    }

    override fun initData() {
        mViewModel.refreshData()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            pointsRank.observe(this@PointsRankActivity, {
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@PointsRankActivity, {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@PointsRankActivity, {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@PointsRankActivity, {
                mBinding.reloadView.root.isVisible = it
            })
        }
    }
}
