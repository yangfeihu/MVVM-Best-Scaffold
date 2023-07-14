package com.xiaojianjun.wanandroid.ui.main.profile

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.FragmentProfileBinding
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.collection.CollectionActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.xiaojianjun.wanandroid.ui.history.HistoryActivity
import com.xiaojianjun.wanandroid.ui.opensource.OpenSourceActivity
import com.xiaojianjun.wanandroid.ui.points.mine.MinePointsActivity
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity
import com.xiaojianjun.wanandroid.ui.settings.SettingsActivity
import com.xiaojianjun.wanandroid.ui.shared.SharedActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding,ProfileViewModel>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun layoutRes() = R.layout.fragment_profile

    override fun viewModelClass() = ProfileViewModel::class.java

    override fun initView() {
        mBinding.clHeader.setOnClickListener {
            checkLogin { /*上传头像，暂不支持*/ }
        }
        mBinding.llMyPoints.setOnClickListener {
            checkLogin { ActivityHelper.startActivity(MinePointsActivity::class.java) }
        }
        mBinding.llPointsRank.setOnClickListener {
            ActivityHelper.startActivity(PointsRankActivity::class.java)
        }
        mBinding.llMyShare.setOnClickListener {
            checkLogin { ActivityHelper.startActivity(SharedActivity::class.java) }
        }
        mBinding.llMyCollect.setOnClickListener {
            checkLogin { ActivityHelper.startActivity(CollectionActivity::class.java) }
        }
        mBinding.llHistory.setOnClickListener {
            ActivityHelper.startActivity(HistoryActivity::class.java)
        }
        mBinding.llAboutAuthor.setOnClickListener {
            ActivityHelper.startActivity(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.my_about_author),
                        link = "https://github.com/jianjunxiao"
                    )
                )
            )
        }
        mBinding.llOpenSource.setOnClickListener {
            ActivityHelper.startActivity(OpenSourceActivity::class.java)
        }
        mBinding.llSetting.setOnClickListener {
            ActivityHelper.startActivity(SettingsActivity::class.java)
        }

        updateUi()
    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            updateUi()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        val isLogin = isLogin()
        val userInfo = UserInfoStore.getUserInfo()
        mBinding.tvLoginRegister.isGone = isLogin
        mBinding.tvNickName.isVisible = isLogin
        mBinding.tvId.isVisible = isLogin
        if (isLogin && userInfo != null) {
            mBinding.tvNickName.text = userInfo.nickname
            mBinding.tvId.text = "ID: ${userInfo.id}"
        }
    }
}
