package com.miyin.zhenbaoqi.ui.login.activity

import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.SubAccountLoginBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.login.contract.SubAccountLoginContract
import com.miyin.zhenbaoqi.ui.login.presenter.SubAccountLoginPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.SubMainActivity
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_sub_account_login.*

class SubAccountLoginActivity : BaseMvpActivity<SubAccountLoginContract.IView, SubAccountLoginContract.IPresenter>(), SubAccountLoginContract.IView {

    private var mAccount: String? = null
    private var mPassword: String? = null

    override fun getContentView() = R.layout.activity_sub_account_login

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("子账号登录")
        immersionBar { statusBarDarkFont(true) }

        et_account.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mAccount = editable.toString().trim { it <= ' ' }
                setLoginBtnEnabled()
            }
        })
        et_password.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPassword = editable.toString().trim { it <= ' ' }
                setLoginBtnEnabled()
            }
        })

        setOnClickListener(iv_eye, btn_login)
    }

    override fun initData() {

    }

    override fun createPresenter() = SubAccountLoginPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_eye -> {
                val method = et_password.transformationMethod
                val isHide = method === HideReturnsTransformationMethod.getInstance()
                if (isHide) {
                    et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
                v.isSelected = !isHide
                // 保证切换后光标位于文本末尾
                val spanText = et_password.text
                if (spanText != null) {
                    Selection.setSelection(spanText, spanText.length)
                }
            }
            R.id.btn_login -> {
                mPresenter?.login(mAccount, mPassword)
            }
        }
    }

    private fun setLoginBtnEnabled() {
        btn_login.isEnabled = !(TextUtils.isEmpty(mAccount) or TextUtils.isEmpty(mPassword))
    }

    override fun loginSuccess(bean: SubAccountLoginBean.DataBean.RoomBean?) {
        ActivityManager.finishActivity(WXLoginActivity::class.java)

        startActivity<SubMainActivity>("bean" to bean)
        finish()
    }

}
