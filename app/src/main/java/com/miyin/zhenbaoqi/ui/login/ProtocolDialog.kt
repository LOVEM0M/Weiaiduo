package com.miyin.zhenbaoqi.ui.login

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.dialog_protocol.*

class ProtocolDialog : BaseDialogFragment() {

    companion object {
        fun newInstance() = ProtocolDialog()
    }

    override fun getContentView() = R.layout.dialog_protocol

    override fun initView(view: View) {
        isCancelable = false
        tv_agree.setOnClickListener {
            SPUtils.putBoolean("is_show_protocol", true)
            dismiss()
        }
        tv_refuse.setOnClickListener { dismiss() }

        SpanUtils.with(tv_title).append("在你使用${getString(R.string.app_name)}前，请你认真阅读并了解")
                .append("《用户协议》").setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        WebActivity.openActivity(context!!, "用户协议", Constant.USER_AGREEMENT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                })
                .append("和")
                .append("《隐私政策》").setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        WebActivity.openActivity(context!!, "隐私政策", Constant.PRIVACY_PROTOCOL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                })
                .append("，点击同意即表示你已阅读并同意全部条款。")
                .create()
        tv_title.highlightColor = Color.TRANSPARENT
    }

    override fun setAnimation() = 0

}
