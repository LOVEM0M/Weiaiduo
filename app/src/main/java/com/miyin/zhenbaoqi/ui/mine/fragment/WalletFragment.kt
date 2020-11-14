package com.miyin.zhenbaoqi.ui.mine.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.BillBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.activity.wallet.BillDetailActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.WalletAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.WalletContract
import com.miyin.zhenbaoqi.ui.mine.presenter.WalletPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class WalletFragment : BaseListFragment<WalletContract.IView, WalletContract.IPresenter>(), WalletContract.IView {

    private var mType = 0
    private var mList = mutableListOf<BillBean.BillListBean>()
    private lateinit var mAdapter: WalletAdapter

    companion object {
        fun newInstance(type: Int) = WalletFragment().apply {
            arguments = Bundle().apply {
                putInt("type", type)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mType = getInt("type")
        }
        return R.layout.fragment_wallet
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = WalletAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { _, _, position ->
                val data = mList[position]
                if (data.change_type != 1) {
                    startActivity<BillDetailActivity>("type" to data.change_type, "order_no" to data.change_number,
                            "price" to data.change_amount.toLong())
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getBillList(mType, mPage, mCount)
    }

    override fun createPresenter() = WalletPresenter()

    override fun reload() {
        mPage = 1
        initData()
    }

    override fun getBillListSuccess(bean: BillBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            smart_refresh_layout.setEnableAutoLoadMore(current_page != pages)
        }
    }

}
