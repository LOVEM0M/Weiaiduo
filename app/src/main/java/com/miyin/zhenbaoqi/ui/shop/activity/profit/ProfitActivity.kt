package com.miyin.zhenbaoqi.ui.shop.activity.profit

import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_profit.*

class ProfitActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_profit

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("累计收益")
        immersionBar { statusBarDarkFont(true) }

        tv_grand_total_income.text = SpanUtils()
                .appendLine("1000.00 ").setFontSize(25, true).setBold()
                .append("累计收益（元）")
                .create()
        tv_wait_credit_income.text = SpanUtils()
                .appendLine("5356.00 ").setFontSize(25, true).setBold()
                .append("待入账收益（元）")
                .create()

        setOnClickListener(fl_goods_brokerage, fl_invite_reward, fl_service_reward)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_goods_brokerage -> startActivity<GoodsBrokerageActivity>()
            R.id.fl_invite_reward -> startActivity<InviteRewardActivity>()
            R.id.fl_service_reward -> startActivity<ServiceRewardActivity>()
        }
    }

}
