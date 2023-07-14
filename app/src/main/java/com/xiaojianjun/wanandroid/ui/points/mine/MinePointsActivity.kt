package com.xiaojianjun.wanandroid.ui.points.mine

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.ActivityMinePointsBinding
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity

class MinePointsActivity : BaseActivity<ActivityMinePointsBinding,MinePointsViewModel>() {

    private lateinit var mAdapter: MinePointsAdapter
    private lateinit var mHeaderView: View

    override fun layoutRes() = R.layout.activity_mine_points

    override fun viewModelClass() = MinePointsViewModel::class.java

    override fun initView() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_mine_points, null)
        mAdapter = MinePointsAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreRecord() }
            mBinding.recyclerView.adapter = it
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        mBinding.ivBack.setOnClickListener { ActivityHelper.finish(MinePointsActivity::class.java) }
        mBinding.ivRank.setOnClickListener { ActivityHelper.startActivity(PointsRankActivity::class.java) }
        mBinding.reloadView.btnReload.setOnClickListener { mViewModel.refresh() }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            totalPoints.observe(this@MinePointsActivity) {
                if (mAdapter.headerLayoutCount == 0) {
                    mAdapter.setHeaderView(mHeaderView)
                }
                mHeaderView.findViewById<TextView>(R.id.tvTotalPoints).text = it.coinCount.toString()
                mHeaderView.findViewById<TextView>(R.id.tvLevelRank).text = getString(R.string.level_rank, it.level, it.rank)
            }
            pointsList.observe(this@MinePointsActivity) {
                mAdapter.setList(it)
            }
            refreshStatus.observe(this@MinePointsActivity) {
                mBinding.swipeRefreshLayout.isRefreshing = it
            }
            loadMoreStatus.observe(this@MinePointsActivity) {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            }
            reloadStatus.observe(this@MinePointsActivity) {
                mBinding.reloadView.root.isVisible = it
            }
        }
    }
}
