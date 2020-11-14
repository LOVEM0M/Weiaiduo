package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.MerchantPayList
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.activity.purse.EarnDetailActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.IncomeDetailAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.IncomeDetailContract
import com.miyin.zhenbaoqi.ui.shop.presenter.IncomeDetailPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class IncomeDetailFragment : BaseListFragment<IncomeDetailContract.IView, IncomeDetailContract.IPresenter>(), IncomeDetailContract.IView {

    private var mList = mutableListOf<MerchantPayList.ListBean>()
    private lateinit var mAdapter: IncomeDetailAdapter
    private var mState = 0
    private var mTag = 0

    companion object {
        fun newInstance(state: Int, tag: Int) = IncomeDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
                putInt("tag", tag)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
            mTag = getInt("tag")
        }
        return R.layout.fragment_income_detail
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()
        recycler_view.run {
            mAdapter = IncomeDetailAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { _, _, position ->
                startActivity<EarnDetailActivity>("order_number" to mList[position].order_number, "tag" to mTag)
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getWallerMerchantPayList(mState, mPage, mCount, mTag)
    }

    override fun createPresenter() = IncomeDetailPresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    override fun getWallerMerchantPayListSuccess(bean: MerchantPayList) {
        with(bean) {
            if (current_page == 1) {
                mList = bean.list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(bean.list!!)
            }
            smart_refresh_layout.setEnableLoadMore(current_page != pages)
        }
    }

}
