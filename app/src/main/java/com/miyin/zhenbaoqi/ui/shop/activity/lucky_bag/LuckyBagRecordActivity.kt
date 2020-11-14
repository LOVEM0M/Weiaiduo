package com.miyin.zhenbaoqi.ui.shop.activity.lucky_bag

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.LuckyBagRecordAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.LuckyBagRecordContract
import com.miyin.zhenbaoqi.ui.shop.presenter.LuckyBagRecordPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class LuckyBagRecordActivity : BaseListActivity<LuckyBagRecordContract.IView, LuckyBagRecordContract.IPresenter>(), LuckyBagRecordContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: LuckyBagRecordAdapter

    override fun getContentView() = R.layout.activity_lucky_bag_record

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("福袋记录")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = LuckyBagRecordAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            startActivity<LuckyBagDetailActivity>()
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = LuckyBagRecordPresenter()

}
