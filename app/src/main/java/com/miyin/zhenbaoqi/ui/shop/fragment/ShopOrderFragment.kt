package com.miyin.zhenbaoqi.ui.shop.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.MerchantOrderBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.shop.activity.order.ShopOrderDetailActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ShopOrderAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ShopOrderContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ShopOrderPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class ShopOrderFragment : BaseListFragment<ShopOrderContract.IView, ShopOrderContract.IPresenter>(), ShopOrderContract.IView {

    private var mState = -1
    private var mList = mutableListOf<MerchantOrderBean.ListBean>()
    private lateinit var mAdapter: ShopOrderAdapter
    private var mSearchVal: String? = null

    companion object {
        fun newInstance(state: Int) = ShopOrderFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_shop_order
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        if (mState != -1) {
            showLoading()
        }

        recycler_view.run {
            mAdapter = ShopOrderAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            val bean = mList[position]
            startActivityForResult<ShopOrderDetailActivity>(Constant.INTENT_REQUEST_CODE, "order_number" to bean.order_number,
                    "avatar" to bean.head_img, "remark" to bean.remark)
        }
        refreshAndLoadMore(smart_refresh_layout) {
            if (mState != -1) {
                initData()
            } else {
                setSearchVal(mSearchVal)
            }
        }
    }

    override fun initData() {
        if (mState != -1) {
            mPresenter?.getOrderList(mPage, 10, mState)
        }
    }

    override fun createPresenter() = ShopOrderPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    fun setSearchVal(content: String?) {
        mSearchVal = content
        if (content.isNullOrEmpty()) {
            return
        }

        showLoading()
        mPresenter?.merchantOrderSearch(1, 50, content!!)
    }

    override fun getOrderListSuccess(bean: MerchantOrderBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

    override fun merchantOrderSearchSuccess(bean: MerchantOrderBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
        }
    }

}
