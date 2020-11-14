package com.miyin.zhenbaoqi.ui.mine.activity.wallet

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_recharge.*

class RechargeActivity : BaseActivity() {

    private var mIsSelectProtocol = false
    private var mPrice: String? = null

    override fun getContentView(): Int {
        return R.layout.activity_recharge
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("余额充值")
        immersionBar { statusBarDarkFont(true) }

        flow_layout.setAdapter(object : TagAdapter(listOf("888", "1000", "3000", "5000", "8000", "10000")) {
            @SuppressLint("SetTextI18n")
            override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                layoutParams = FrameLayout.LayoutParams(getDimensionPixelSize(R.dimen.dp_110), getDimensionPixelSize(R.dimen.dp_30)).apply {
                    marginEnd = getDimensionPixelSize(R.dimen.dp_8)
                    topMargin = getDimensionPixelSize(R.dimen.dp_10)
                }
                gravity = Gravity.CENTER
                text = "¥ $data"
                setTextColor(DrawableCreator.Builder()
                        .setCheckedTextColor(ContextCompat.getColor(context, R.color.text_red))
                        .setUnCheckedTextColor(Color.BLACK)
                        .buildTextColor())
                background = DrawableCreator.Builder()
                        .setStrokeWidth(getDimension(R.dimen.dp_1))
                        .setCheckedStrokeColor(ContextCompat.getColor(context, R.color.text_red), ContextCompat.getColor(context, R.color.bg_f5))
                        .setCornersRadius(getDimension(R.dimen.dp_5))
                        .build()
            }
        })

        SpanUtils.with(tv_protocol)
                .append("我已阅读并同意")
                .append("《余额充值协议》")
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(view: View) {
                        WebActivity.openActivity(applicationContext, "余额充值协议", Constant.RECHARGE)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.run {
                            isUnderlineText = false
                            color = Color.parseColor("#3D9FE8")
                        }
                    }
                })
                .create()
        tv_protocol.highlightColor = Color.TRANSPARENT

        et_money.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPrice = editable.toString().trim { it <= ' ' }
            }
        })
        setOnClickListener(btn_commit, iv_protocol)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_protocol -> {
                mIsSelectProtocol = !mIsSelectProtocol
                iv_protocol.isSelected = mIsSelectProtocol
            }
            R.id.btn_commit -> {
            }
        }
    }

}
