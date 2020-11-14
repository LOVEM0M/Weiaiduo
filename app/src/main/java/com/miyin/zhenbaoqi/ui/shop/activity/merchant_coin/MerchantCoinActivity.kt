package com.miyin.zhenbaoqi.ui.shop.activity.merchant_coin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.MerchantCoinAdapter
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_merchant_coin.*

class MerchantCoinActivity : BaseActivity() {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: MerchantCoinAdapter

    override fun getContentView() = R.layout.activity_merchant_coin

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("商家币")
        immersionBar { statusBarDarkFont(true) }

        tv_merchant_coin.text = SpanUtils()
                .appendLine("1000").setFontSize(30, true)
                .append("当前玩家币")
                .create()
        recycler_view.run {
            repeat((0..3).count()) { mList.add("") }
            mAdapter = MerchantCoinAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_exchange) {
                    startActivity<ExchangeDetailActivity>()
                }
            }
        }

        setOnClickListener(tv_exchange_record, tv_task_record)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_exchange_record -> startActivity<ExchangeRecordActivity>()
            R.id.tv_task_record -> {
            }
        }
    }

}
