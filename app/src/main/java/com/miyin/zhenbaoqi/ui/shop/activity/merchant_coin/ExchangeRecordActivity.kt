package com.miyin.zhenbaoqi.ui.shop.activity.merchant_coin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ExchangeRecordAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ExchangeRecordContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ExchangeRecordPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class ExchangeRecordActivity : BaseListActivity<ExchangeRecordContract.IView, ExchangeRecordContract.IPresenter>(), ExchangeRecordContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: ExchangeRecordAdapter

    override fun getContentView() = R.layout.activity_exchange_record

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("兑换记录")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            repeat((0..10).count()) { mList.add("") }
            mAdapter = ExchangeRecordAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = ExchangeRecordPresenter()

}
