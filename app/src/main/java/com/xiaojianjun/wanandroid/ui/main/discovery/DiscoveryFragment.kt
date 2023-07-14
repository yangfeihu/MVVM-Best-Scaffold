package com.xiaojianjun.wanandroid.ui.main.discovery

import androidx.core.view.isGone
import androidx.core.view.isVisible
import coil.load
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.FragmentDiscoveryBinding
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.BannerData
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import com.xiaojianjun.wanandroid.ui.search.SearchActivity
import com.xiaojianjun.wanandroid.ui.share.ShareActivity
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder



class DiscoveryFragment : BaseFragment<FragmentDiscoveryBinding,DiscoveryViewModel>(), ScrollToTop {

    private lateinit var hotWordsAdapter: HotWordsAdapter

    private val myBannerView: Banner<BannerData, BannerImageAdapter<BannerData>> by lazy { requireView().findViewById(R.id.bannerView) }


    companion object {
        fun newInstance() = DiscoveryFragment()
    }

    override fun layoutRes() = R.layout.fragment_discovery

    override fun viewModelClass() = DiscoveryViewModel::class.java

    override fun initView() {
        mBinding.ivAdd.setOnClickListener {
            checkLogin { ActivityHelper.startActivity(ShareActivity::class.java) }
        }
        mBinding.ivSearch.setOnClickListener {
            ActivityHelper.startActivity(SearchActivity::class.java)
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getData() }
        }

        hotWordsAdapter = HotWordsAdapter(R.layout.item_hot_word).apply {
            setOnItemClickListener { _, _, position ->
                ActivityHelper.startActivity(
                    SearchActivity::class.java, mapOf(
                        SearchActivity.SEARCH_WORDS to data[position].name
                    )
                )
            }
            mBinding.rvHotWord.adapter = this
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getData()
        }
        mBinding.nestedScollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
    }

    override fun observe() {
        super.observe()

        mViewModel.run {
            banners.observe(viewLifecycleOwner) {
                setupBanner(it)
            }
            hotWords.observe(viewLifecycleOwner) {
                hotWordsAdapter.setList(it)
                mBinding.tvHotWordTitle.isVisible = it.isNotEmpty()
            }
            frequentlyList.observe(viewLifecycleOwner) {
                mBinding.tagFlowLayout.adapter = TagAdapter(it)
                mBinding.tagFlowLayout.setOnTagClickListener { _, position, _ ->
                    val frequently = it[position]
                    ActivityHelper.startActivity(
                        DetailActivity::class.java,
                        mapOf(
                            PARAM_ARTICLE to Article(
                                title = frequently.name,
                                link = frequently.link
                            )
                        )
                    )
                    false
                }
                mBinding.tvFrquently.isGone = it.isEmpty()
            }
            refreshStatus.observe(viewLifecycleOwner) {
                mBinding.swipeRefreshLayout.isRefreshing = it
            }
            reloadStatus.observe(viewLifecycleOwner) {
                mBinding.reloadView.root.isVisible = it
            }
        }
    }

     private fun setupBanner(list: List<BannerData>) {



//         myBannerView.setOnBannerListener { data, _ ->
//             ActivityHelper.startActivity(
//                 DetailActivity::class.java,
//                 mapOf(PARAM_ARTICLE to Article(title = data.title, link = data.url))
//             )
//         }

         myBannerView.setAdapter(object : BannerImageAdapter<BannerData>(list) {
             override fun onBindView(
                 holder: BannerImageHolder?,
                 data: BannerData?,
                 position: Int,
                 size: Int
             ) {
                 holder?.imageView?.load(data?.imagePath){
                 }
             }
         })

    }



    override fun initData() {
        mViewModel.getData()
    }

    override fun scrollToTop() {
        mBinding.nestedScollView?.smoothScrollTo(0, 0)
    }

    override fun onResume() {
        super.onResume()
        //bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        //bannerView.stopAutoPlay()
    }
}
