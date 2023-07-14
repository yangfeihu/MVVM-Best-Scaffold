package com.xiaojianjun.wanandroid.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.CommonActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.ActivitySearchBinding
import com.xiaojianjun.wanandroid.ext.hideSoftInput
import com.xiaojianjun.wanandroid.ui.search.history.SearchHistoryFragment
import com.xiaojianjun.wanandroid.ui.search.result.SearchResultFragment

class SearchActivity : CommonActivity() {



    companion object {
        const val SEARCH_WORDS = "search_words"
    }

    override fun layoutRes() = R.layout.activity_search


    lateinit var mBinding: ActivitySearchBinding
    override fun initDataViewBinding(): Boolean {
        mBinding = DataBindingUtil.setContentView(this, layoutRes())
        //binding.setVariable(BR.viewModel, mViewModel)
        mBinding.lifecycleOwner = this
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val historyFragment = SearchHistoryFragment.newInstance()
        val resultFragment = SearchResultFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, historyFragment)
            .add(R.id.flContainer, resultFragment)
            .show(historyFragment)
            .hide(resultFragment)
            .commit()

        mBinding.ivBack.setOnClickListener {
            if (resultFragment.isVisible) {
                supportFragmentManager
                    .beginTransaction()
                    .hide(resultFragment)
                    .commit()
            } else {
                ActivityHelper.finish(SearchActivity::class.java)
            }
        }
        mBinding.ivDone.setOnClickListener {
            val searchWords = mBinding.acetInput.text.toString()
            if (searchWords.isEmpty()) return@setOnClickListener
            it.hideSoftInput()
            historyFragment.addSearchHistory(searchWords)
            resultFragment.doSearch(searchWords)
            supportFragmentManager
                .beginTransaction()
                .show(resultFragment)
                .commit()
        }
        mBinding.acetInput.run {
            addTextChangedListener(afterTextChanged = {
                mBinding.ivClearSearch.isGone = it.isNullOrEmpty()
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mBinding.ivDone.performClick()
                    true
                } else {
                    false
                }
            }
        }
        mBinding.ivClearSearch.setOnClickListener {  mBinding.acetInput.setText("") }

        // 跳转搜索
        intent.getStringExtra(SEARCH_WORDS)?.let { window.decorView.post { fillSearchInput(it) } }
    }

    fun fillSearchInput(keywords: String) {
        mBinding.acetInput.setText(keywords)
        mBinding.acetInput.setSelection(keywords.length)
        mBinding.ivDone.performClick()
    }

    override fun onBackPressed() {
        mBinding.ivBack.performClick()
    }

}
