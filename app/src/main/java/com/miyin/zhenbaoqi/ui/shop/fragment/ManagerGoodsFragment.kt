package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.ui.shop.adapter.ManagerGoodsAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerGoodsContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ManagerGoodsPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

@Deprecated("")
class ManagerGoodsFragment : BaseListFragment<ManagerGoodsContract.IView, ManagerGoodsContract.IPresenter>(), ManagerGoodsContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: ManagerGoodsAdapter

    companion object {
        fun newInstance() = ManagerGoodsFragment()
    }

    override fun getContentView(): Int {
        return R.layout.fragment_manager_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = ManagerGoodsAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position -> {

        } }

    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = ManagerGoodsPresenter()

}
