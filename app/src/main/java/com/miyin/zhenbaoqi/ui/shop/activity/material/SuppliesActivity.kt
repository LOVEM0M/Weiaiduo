package com.miyin.zhenbaoqi.ui.shop.activity.material

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.SuppliesAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.SuppliesContract
import com.miyin.zhenbaoqi.ui.shop.presenter.SuppliesPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class SuppliesActivity : BaseListActivity<SuppliesContract.IView, SuppliesContract.IPresenter>(), SuppliesContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: SuppliesAdapter

    override fun getContentView() = R.layout.activity_supplies

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("物料中心")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            repeat((0..10).count()) { mList.add("") }
            mAdapter = SuppliesAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {

    }

    override fun createPresenter() = SuppliesPresenter()

}
