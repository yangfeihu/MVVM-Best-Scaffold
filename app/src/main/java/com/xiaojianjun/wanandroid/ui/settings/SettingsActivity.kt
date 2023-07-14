package com.xiaojianjun.wanandroid.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.BuildConfig
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.simple.SeekBarChangeListenerAdapter
import com.xiaojianjun.wanandroid.databinding.ActivitySettingsBinding
import com.xiaojianjun.wanandroid.ext.setNavigationBarColor
import com.xiaojianjun.wanandroid.ext.showToast
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.xiaojianjun.wanandroid.ui.login.LoginActivity
import com.xiaojianjun.wanandroid.ui.test.TestActivity
import com.xiaojianjun.wanandroid.util.clearCache
import com.xiaojianjun.wanandroid.util.getCacheSize
import com.xiaojianjun.wanandroid.util.isNightMode
import com.xiaojianjun.wanandroid.util.setNightMode

@SuppressLint("SetTextI18n")
class SettingsActivity : BaseActivity<ActivitySettingsBinding,SettingsViewModel>() {

    override fun layoutRes() = R.layout.activity_settings

    override fun viewModelClass() = SettingsViewModel::class.java

    override fun initView() {

        setNavigationBarColor(getColor(R.color.bgColorSecondary))

        mBinding.scDayNight.isChecked = isNightMode(this)
        mBinding.tvFontSize.text = "${SettingsStore.getWebTextZoom()}%"
        mBinding.tvClearCache.text = getCacheSize(this)
        mBinding.tvAboutUs.text = getString(R.string.current_version, BuildConfig.VERSION_NAME)

        mBinding.ivBack.setOnClickListener { ActivityHelper.finish(SettingsActivity::class.java) }
        mBinding.scDayNight.setOnCheckedChangeListener { _, checked ->
            setNightMode(checked)
            SettingsStore.setNightMode(checked)
        }
        mBinding.llFontSize.setOnClickListener {
            setFontSize()
        }
        mBinding.llClearCache.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_clear_cache)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    clearCache(this)
                    mBinding.tvClearCache.text = getCacheSize(this)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
        mBinding.llCheckVersion.setOnClickListener {
            // TODO 检查版本
            showToast(getString(R.string.stay_tuned))
        }
        mBinding.llAboutUs.setOnClickListener {
            ActivityHelper.startActivity(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.abount_us),
                        link = "https://github.com/jianjunxiao/wanandroid"
                    )
                )
            )
        }
        mBinding.tvLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    mViewModel.logout()
                    ActivityHelper.startActivity(LoginActivity::class.java)
                    ActivityHelper.finish(SettingsActivity::class.java)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
        mBinding.tvLogout.isVisible = isLogin()
        mBinding.ivTest.setOnClickListener { ActivityHelper.startActivity(TestActivity::class.java) }
    }

    @SuppressLint("InflateParams")
    private fun setFontSize() {
        val textZoom = SettingsStore.getWebTextZoom()
        var tempTextZoom = textZoom
        AlertDialog.Builder(this)
            .setTitle(R.string.font_size)
            .setView(LayoutInflater.from(this).inflate(R.layout.view_change_text_zoom, null).apply {

                val seekBar = this.findViewById<AppCompatSeekBar>(R.id.seekBar)
                seekBar.progress = textZoom - 50
                seekBar.setOnSeekBarChangeListener(SeekBarChangeListenerAdapter(
                    onProgressChanged = { _, progress, _ ->
                        tempTextZoom = 50 + progress
                        mBinding.tvFontSize.text = "$tempTextZoom%"
                    }
                ))
            })
            .setNegativeButton(R.string.cancel) { _, _ ->
                mBinding.tvFontSize.text = "$textZoom%"
            }
            .setPositiveButton(R.string.confirm) { _, _ ->
                SettingsStore.setWebTextZoom(tempTextZoom)
            }
            .show()
    }
}
