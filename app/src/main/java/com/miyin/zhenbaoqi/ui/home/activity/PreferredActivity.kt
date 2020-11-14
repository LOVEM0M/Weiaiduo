package com.miyin.zhenbaoqi.ui.home.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.ui.home.adapter.PreferredAdapter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ui.home.contract.PreferredContract
import com.miyin.zhenbaoqi.ui.home.presenter.PreferredPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class PreferredActivity :BaseListActivity<PreferredContract.IView, PreferredContract.IPresenter>(), PreferredContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: PreferredAdapter

    override fun getContentView() = R.layout.activity_preferred

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("优选店铺")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = PreferredAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = PreferredPresenter()

}
