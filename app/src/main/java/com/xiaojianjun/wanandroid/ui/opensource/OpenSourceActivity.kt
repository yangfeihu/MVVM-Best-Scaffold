package com.xiaojianjun.wanandroid.ui.opensource

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.CommonActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.ActivityOpenSourceBinding
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE

class OpenSourceActivity : CommonActivity() {

    private val openSourceData = listOf(
        Article(
            title = "OkHttp",
            link = "https://github.com/square/okhttp"
        ),
        Article(
            title = "Retrofit",
            link = "https://github.com/square/retrofit"
        ),
        Article(
            title = "BaseRecyclerViewAdapterHelper",
            link = "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
        ),
        Article(
            title = "FlowLayout",
            link = "https://github.com/hongyangAndroid/FlowLayout"
        ),
        Article(
            title = "Banner",
            link = "https://github.com/youth5201314/banner"
        ),
        Article(
            title = "Glide",
            link = "https://github.com/bumptech/glide"
        ),
        Article(
            title = "Glide-Tansformations",
            link = "https://github.com/wasabeef/glide-transformations"
        ),
        Article(
            title = "AgentWeb",
            link = "https://github.com/Justson/AgentWeb"
        ),
        Article(
            title = "LiveEventBus",
            link = "https://github.com/JeremyLiao/LiveEventBus"
        ),
        Article(
            title = "PersistentCookieJar",
            link = "https://github.com/franmontiel/PersistentCookieJar"
        )
    )

    override fun layoutRes() = R.layout.activity_open_source


    lateinit var mBinding:ActivityOpenSourceBinding
    override fun initDataViewBinding(): Boolean {
        mBinding = DataBindingUtil.setContentView(this, layoutRes())
        //binding.setVariable(BR.viewModel, mViewModel)
        mBinding.lifecycleOwner = this
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OpenSourceAdapter().also {
            it.setList(openSourceData)
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(DetailActivity::class.java, mapOf(PARAM_ARTICLE to article))
            }
            mBinding.recyclerView.adapter = it
        }

        mBinding.ivBack.setOnClickListener { ActivityHelper.finish(OpenSourceActivity::class.java) }
    }
}
