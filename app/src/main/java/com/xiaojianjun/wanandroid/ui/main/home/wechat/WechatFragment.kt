package com.xiaojianjun.wanandroid.ui.main.home.wechat

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.databinding.FragmentWechatBinding
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.CategoryAdapter
import com.xiaojianjun.wanandroid.ui.main.home.SimpleArticleAdapter


class WechatFragment : BaseFragment<FragmentWechatBinding,WechatViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = WechatFragment()
    }

    private lateinit var mAdapter: SimpleArticleAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun layoutRes() = R.layout.fragment_wechat

    override fun viewModelClass() = WechatViewModel::class.java

    override fun initView() {
        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListeners()
    }

    private fun initRefresh() {
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshWechatArticleList() }
        }
    }

    private fun initCategoryAdapter() {
        mCategoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.onCheckedListener = { position ->
                mViewModel.refreshWechatArticleList(position)
            }
            mBinding.rvCategory.adapter = it
        }
    }

    private fun initArticleAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreWechatArticleList()
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
            mBinding.recyclerView.adapter = it
        }
    }

    private fun initListeners() {
        mBinding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshWechatArticleList()
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getWechatCategory()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.categories.observe(viewLifecycleOwner) {
            updateCategory(it)
        }
        mViewModel.checkedCategory.observe(viewLifecycleOwner) {
            mCategoryAdapter.check(it)
        }
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
        mViewModel.reloadListStatus.observe(viewLifecycleOwner) {
            mBinding.reloadListView.root.isVisible = it
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    private fun updateCategory(categoryList: MutableList<Category>) {
        mBinding.rvCategory.isGone = categoryList.isEmpty()
        mCategoryAdapter.setList(categoryList)
    }

    override fun lazyLoadData() {
        mViewModel.getWechatCategory()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}
