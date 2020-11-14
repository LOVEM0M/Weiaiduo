package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.ui.shop.adapter.GoodsBrokerageAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.GoodsBrokerageContract
import com.miyin.zhenbaoqi.ui.shop.presenter.GoodsBrokeragePresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class GoodsBrokerageFragment : BaseListFragment<GoodsBrokerageContract.IView, GoodsBrokerageContract.IPresenter>(), GoodsBrokerageContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: GoodsBrokerageAdapter

    companion object {
        fun newInstance() = GoodsBrokerageFragment()
    }

    override fun getContentView() = R.layout.fragment_goods_brokerage

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = GoodsBrokerageAdapter(mList)
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

    override fun createPresenter() = GoodsBrokeragePresenter()

}
