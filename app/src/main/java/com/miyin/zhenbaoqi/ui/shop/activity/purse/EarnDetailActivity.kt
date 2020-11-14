package com.miyin.zhenbaoqi.ui.shop.activity.purse

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.MerchantEarnDetail
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ui.shop.contract.EarnDetailContract
import com.miyin.zhenbaoqi.ui.shop.presenter.EarnDetailPresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_earn_detail.*

@SuppressLint("SetTextI18n")
class EarnDetailActivity : BaseMvpActivity<EarnDetailContract.IView, EarnDetailContract.IPresenter>(), EarnDetailContract.IView {

    private var mOrderNumber: String? = null
    private var mTag = 0

    override fun getContentView(): Int {
        with(intent) {
            mOrderNumber = getStringExtra("order_number")
            mTag = getIntExtra("tag", 0)
        }
        return R.layout.activity_earn_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("交易类型")
        immersionBar { statusBarDarkFont(true) }
    }

    override fun initData() {
        if (mTag == 0) {
            mPresenter?.getEarnDetail(mOrderNumber)
        } else {
            mPresenter?.getPaymentDetail(mOrderNumber)
        }
    }

    override fun createPresenter() = EarnDetailPresenter()

    override fun getEarnDetailSuccess(bean: MerchantEarnDetail) {
        bean.data?.run {
            if (asset_type == 1) {
                tv_price.text = "+" + FormatUtils.formatNumber(change_amount / 100f)
                tv_desc.text = "收入金额（元）"
            } else {
                tv_price.text = "-" + FormatUtils.formatNumber(change_amount / 100f)
                tv_desc.text = "支出金额（元）"
            }
            tv_transaction_type.text = change_name
            tv_state.text = when (type) {
                1 -> "已入账"
                2 -> "待入账"
                3 -> "退款"
                4 -> "支出"
                else -> ""
            }

            SpanUtils.with(tv_order_num)
                    .append(order_number ?: "")
                    .appendSpace(20)
                    .append("复制")
                    .setClickSpan(object : ClickableSpan() {
                        override fun onClick(weight: View) {
                            copyMsg(order_number ?: "")
                            showToast("复制成功")
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.run {
                                isUnderlineText = false
                                color = 0xFF588CB8.toInt()
                            }
                        }
                    })
                    .create()
            tv_order_num.highlightColor = Color.TRANSPARENT
            tv_date_time.text = create_time
        }
    }

}
