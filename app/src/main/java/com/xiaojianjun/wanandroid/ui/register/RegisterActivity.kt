package com.xiaojianjun.wanandroid.ui.register

import android.view.inputmethod.EditorInfo
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.databinding.ActivityRegisterBinding
import com.xiaojianjun.wanandroid.ui.login.LoginActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding,RegisterViewModel>() {

    override fun layoutRes() = R.layout.activity_register

    override fun viewModelClass() = RegisterViewModel::class.java

    override fun initView() {
        mBinding.ivBack.setOnClickListener { ActivityHelper.finish(RegisterActivity::class.java) }
        mBinding.tietConfirmPssword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                mBinding.btnRegister.performClick()
                true
            } else {
                false
            }
        }
        mBinding.btnRegister.setOnClickListener {
            mBinding.tilAccount.error = ""
            mBinding.tilPassword.error = ""
            mBinding.tilConfirmPssword.error = ""

            val account =   mBinding.tietAccount.text.toString()
            val password =   mBinding.tietPassword.text.toString()
            val confirmPassword =   mBinding.tietConfirmPssword.text.toString()

            when {
                account.isEmpty() ->   mBinding.tilAccount.error = getString(R.string.account_can_not_be_empty)
                account.length < 3 ->   mBinding.tilAccount.error =
                    getString(R.string.account_length_over_three)
                password.isEmpty() ->   mBinding.tilPassword.error =
                    getString(R.string.password_can_not_be_empty)
                password.length < 6 ->   mBinding.tilPassword.error =
                    getString(R.string.password_length_over_six)
                confirmPassword.isEmpty() ->   mBinding.tilConfirmPssword.error =
                    getString(R.string.confirm_password_can_not_be_empty)
                password != confirmPassword ->   mBinding.tilConfirmPssword.error =
                    getString(R.string.two_password_are_inconsistent)
                else -> mViewModel.register(account, password, confirmPassword)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            submitting.observe(this@RegisterActivity) {
                if (it) showProgressDialog(R.string.registerring) else dismissProgressDialog()
            }
            registerResult.observe(this@RegisterActivity) {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java, RegisterActivity::class.java)
                }
            }
        }
    }
}
