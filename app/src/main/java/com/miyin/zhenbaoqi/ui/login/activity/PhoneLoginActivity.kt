package com.miyin.zhenbaoqi.ui.login.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.ui.MainActivity
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.login.contract.PhoneLoginContract
import com.miyin.zhenbaoqi.ui.login.presenter.PhoneLoginPresenter
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_phone_login.*

class PhoneLoginActivity : BaseMvpActivity<PhoneLoginContract.IView, PhoneLoginContract.IPresenter>(), PhoneLoginContract.IView {

    private var mPhone: String? = null
    private var mValidateCode: String? = null
    private var mInviteCode: String? = null
    private var mIsSelect = true
    private var mPushCode: String? = null

    override fun getContentView() = R.layout.activity_phone_login

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("手机号登录")
        immersionBar { statusBarDarkFont(true) }
        mPushCode = JPushInterface.getRegistrationID(applicationContext)

        iv_cover.transform(R.drawable.ic_app_logo, RoundedCorners(12))
        SpanUtils.with(tv_protocol)
                .append("已阅读并同意")
                .append("《用户协议》")
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        WebActivity.openActivity(applicationContext, "用户协议", Constant.USER_AGREEMENT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.run {
                            isUnderlineText = false
                            color = 0xFF2F7AB1.toInt()
                        }
                    }
                })
                .append("和")
                .append("《隐私政策》")
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        WebActivity.openActivity(applicationContext, "隐私协议", Constant.PRIVACY_PROTOCOL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.run {
                            isUnderlineText = false
                            color = 0xFF2F7AB1.toInt()
                        }
                    }
                })
                .create()
        tv_protocol.highlightColor = Color.TRANSPARENT
        iv_check.isSelected = mIsSelect

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
        et_invite_code.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mInviteCode = editable.toString().trim { it <= ' ' }
            }
        })
        setOnClickListener(tv_validate_code, btn_login, iv_check)
    }

    override fun initData() {

    }

    override fun createPresenter() = PhoneLoginPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_validate_code -> mPresenter?.sendVerificationCode(mPhone)
            R.id.btn_login -> {
                if (!mIsSelect) {
                    showToast("请勾选用户协议和隐私政策")
                    return
                }
                mPresenter?.login(mValidateCode, mPhone, mPushCode, mInviteCode)
            }
            R.id.iv_check -> {
                mIsSelect = !mIsSelect
                iv_check.isSelected = mIsSelect
            }
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
        val userId = SPUtils.getInt("user_id")
        JPushInterface.setAlias(this, userId, "zbq_$userId")

        SPUtils.putString("phone", mPhone!!)
        startActivity<MainActivity>()
        finish()
    }

}
