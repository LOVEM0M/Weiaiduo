package com.miyin.zhenbaoqi.ui.login.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.MainActivity
import com.miyin.zhenbaoqi.ui.login.contract.BindPhoneContract
import com.miyin.zhenbaoqi.ui.login.presenter.BindPhonePresenter
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_bind_phone.*

class BindPhoneActivity : BaseMvpActivity<BindPhoneContract.IView, BindPhoneContract.IPresenter>(), BindPhoneContract.IView {

    private var mPhone: String? = null
    private var mValidateCode: String? = null

    override fun getContentView() = R.layout.activity_bind_phone

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("手机绑定")
        immersionBar { statusBarDarkFont(true) }

        et_phone.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPhone = editable.toString().trim { it <= ' ' }
                setLoginBtnEnabled()
            }
        })
        et_validate_code.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mValidateCode = editable.toString().trim { it <= ' ' }
                setLoginBtnEnabled()
            }
        })

        setOnClickListener(tv_validate_code, btn_login)
    }

    override fun initData() {

    }

    override fun createPresenter() = BindPhonePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_validate_code -> mPresenter?.sendVerificationCode(mPhone)
            R.id.btn_login -> mPresenter?.login(mValidateCode, mPhone)
        }
    }

    private fun setLoginBtnEnabled() {
        btn_login.isEnabled = !(TextUtils.isEmpty(mPhone) or TextUtils.isEmpty(mValidateCode))
    }

    override fun setCodeBtnEnabled(enabled: Boolean) {
        tv_validate_code.isEnabled = enabled
    }

    override fun setCodeBtnText(title: String) {
        tv_validate_code.text = title
    }

    override fun onLogin() {
        ActivityManager.finishActivity(WXLoginActivity::class.java)
        val userId = SPUtils.getInt("userId")
        JPushInterface.setAlias(this, userId, "zbq_$userId")

        SPUtils.putString("phone", mPhone!!)
        startActivity<MainActivity>()
        finish()
    }

}
