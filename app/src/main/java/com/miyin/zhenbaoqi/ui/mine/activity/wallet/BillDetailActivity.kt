package com.miyin.zhenbaoqi.ui.mine.activity.wallet

import android.annotation.SuppressLint
import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.BillPayBean
import com.miyin.zhenbaoqi.bean.BillWithdrawBean
import com.miyin.zhenbaoqi.ui.mine.contract.BillDetailContract
import com.miyin.zhenbaoqi.ui.mine.presenter.BillDetailPresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_bill_detail.*

@SuppressLint("SetTextI18n")
class BillDetailActivity : BaseMvpActivity<BillDetailContract.IView, BillDetailContract.IPresenter>(), BillDetailContract.IView {

    private var mType = 0
    private var mChangeNumber: String? = null
    private var mPrice = 0L

    override fun getContentView(): Int {
        with(intent) {
            mType = getIntExtra("type", 0)
            mChangeNumber = getStringExtra("order_no")
            mPrice = getLongExtra("price", 0L)
        }
        return R.layout.activity_bill_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("账单详情")
        immersionBar { statusBarDarkFont(true) }

        tv_status.text = "交易成功"
    }

    override fun initData() {
        when (mType) {
            1 -> mPresenter?.incomeDetail(mChangeNumber ?: "")
            2 -> mPresenter?.withdrawDetail(mChangeNumber ?: "")
            3 -> mPresenter?.payDetail(mChangeNumber ?: "")
        }
    }

    override fun createPresenter() = BillDetailPresenter()

    override fun withdrawDetailSuccess(bean: BillWithdrawBean) {
        tv_left_title.text = "用户名\n提现金额\n提现时间\n订单号\n提现卡号"

        with(bean) {
            tv_name.text = "${real_name}自己提现申请"
            tv_price.text = "-${FormatUtils.formatNumber(mPrice  )}"
            tv_status.text = when (state) {
                1 -> "提现中"
                2 -> "提现成功"
                else -> "提现失败"
            }

            tv_desc.text = "$real_name\n${FormatUtils.formatNumber(amount  )}\n$cash_time\n$cash_number\n${if (state == 2) bank_sn else "$bank_name $card_no"}"
        }
    }

    override fun incomeDetailSuccess() {
        tv_left_title.text = "用户名\n消费金额\n消费时间\n订单号\n奖励金额"
    }

    override fun payDetailSuccess(bean: BillPayBean) {
        tv_left_title.text = "购买商品\n商家信息\n订单金额\n支付时间\n订单号\n付款方式\n收货信息"

        with(bean) {
            tv_desc.text = "$goods_name\n$merchants_name\n${FormatUtils.formatNumber(mPrice  )}\n${TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm")}\n$mChangeNumber\n${if (pay_type == 1) "余额支付" else if (pay_type == 2) "支付宝支付" else "微信支付"}\n$address"
            tv_user_info.text = consignee + phone_no

            tv_name.text = "${consignee}自己消费情况"
            tv_price.text = "-${FormatUtils.formatNumber(mPrice  )}"
        }
    }

}

