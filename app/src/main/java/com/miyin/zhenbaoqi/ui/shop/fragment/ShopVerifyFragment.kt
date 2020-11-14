package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.ui.shop.adapter.ShopVerifyAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ShopVerifyContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ShopVerifyPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class ShopVerifyFragment : BaseListFragment<ShopVerifyContract.IView, ShopVerifyContract.IPresenter>(), ShopVerifyContract.IView {

    private var mTitle: String? = null
    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: ShopVerifyAdapter

    companion object {
        fun newInstance(title: String) = ShopVerifyFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mTitle = getString("title")
        }
        return R.layout.fragment_shop_verify
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ShopVerifyAdapter(mList, mTitle)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = ShopVerifyPresenter()

}
