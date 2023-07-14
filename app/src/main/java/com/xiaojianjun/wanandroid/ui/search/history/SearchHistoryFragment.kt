package com.xiaojianjun.wanandroid.ui.search.history

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.databinding.FragmentSearchHistoryBinding
import com.xiaojianjun.wanandroid.model.bean.HotWord
import com.xiaojianjun.wanandroid.ui.search.SearchActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter


class SearchHistoryFragment : BaseFragment<FragmentSearchHistoryBinding,SearchHistoryViewModel>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun layoutRes() = R.layout.fragment_search_history

    override fun viewModelClass() = SearchHistoryViewModel::class.java

    override fun initView() {
        val fragmentActivity = activity as? SearchActivity ?: return
        searchHistoryAdapter = SearchHistoryAdapter(fragmentActivity).also {
            it.onItemClickListener = { position ->
                fragmentActivity.fillSearchInput(it.data[position])
            }
            it.onDeleteClickListener = { position ->
                mViewModel.deleteSearchHistory(it.data[position])
            }
            mBinding.rvSearchHistory.adapter = it
        }
    }

    override fun initData() {
        mViewModel.getHotSearch()
        mViewModel.getSearchHistory()
    }

    override fun observe() {
        super.observe()
        mViewModel.hotWords.observe(viewLifecycleOwner) {
            mBinding.tvHotSearch.visibility = View.VISIBLE
            setHotwords(it)
        }
        mViewModel.searchHistory.observe(viewLifecycleOwner) {
            mBinding.tvSearchHistory.isGone = it.isEmpty()
            searchHistoryAdapter.submitList(it)
        }
    }

    private fun setHotwords(hotwords: List<HotWord>) {
        mBinding.tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(hotwords) {
                override fun getView(parent: FlowLayout?, position: Int, hotWord: HotWord?): View {
                    return LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_hot_search, parent, false)
                        .apply {
                            this.findViewById<TextView>(R.id.tvTag).text = hotWord?.name
                        }
                }
            }
            setOnTagClickListener { _, position, _ ->
                (activity as? SearchActivity)?.fillSearchInput(hotwords[position].name)
                false
            }
        }
    }

    fun addSearchHistory(keywords: String) {
        mViewModel.addSearchHistory(keywords)
    }

}
