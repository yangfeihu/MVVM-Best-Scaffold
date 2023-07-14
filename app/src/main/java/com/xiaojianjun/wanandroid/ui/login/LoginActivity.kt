package com.xiaojianjun.wanandroid.ui.login

import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.ActivityLoginBinding
import com.xiaojianjun.wanandroid.ui.register.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>() {

    override fun layoutRes() = R.layout.activity_login

    override fun viewModelClass() = LoginViewModel::class.java

    override fun initView() {

        mBinding.ivClose.setOnClickListener {
            ActivityHelper.finish(LoginActivity::class.java)
        }
        mBinding.tvGoRegister.setOnClickListener {
            ActivityHelper.startActivity(RegisterActivity::class.java)
        }
        mBinding.tietPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_GO) {
                mBinding.btnLogin.performClick()
                true
            } else {
                false
            }
        }
        mBinding.btnLogin.setOnClickListener {
            mBinding.tilAccount.error = ""
            mBinding.tilPassword.error = ""
            val account = mBinding.tietAccount.text.toString()
            val password = mBinding.tietPassword.text.toString()
            when {
                account.isEmpty() -> mBinding.tilAccount.error = getString(R.string.account_can_not_be_empty)
                password.isEmpty() -> mBinding.tilPassword.error =
                    getString(R.string.password_can_not_be_empty)
                else -> mViewModel.login(account, password)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            submitting.observe(this@LoginActivity) {
                if (it) showProgressDialog(R.string.logging_in) else dismissProgressDialog()
            }
            loginResult.observe(this@LoginActivity) {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java)
                }
            }
        }
    }
}
