package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ManagerAuctionAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerAuctionContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ManagerAuctionPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class ManagerAuctionFragment : BaseListFragment<ManagerAuctionContract.IView, ManagerAuctionContract.IPresenter>(), ManagerAuctionContract.IView {

    private var mState = 0
    private var mList = mutableListOf<MerchantGoodsBean.ListBean>()
    private lateinit var mAdapter: ManagerAuctionAdapter

    companion object {
        fun newInstance(state: Int) = ManagerAuctionFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_manager_auction
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ManagerAuctionAdapter(mList, mState)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                when (view.id) {
                    R.id.cl_container -> {
                        startActivity<AuctionDetailActivity>("goods_id" to mList[position].goods_id)
                    }
                    R.id.tv_edit -> {
                        //mPresenter?.updateMerchantGoodsState(bean.goods_id, )
                    }
                    R.id.tv_delete -> {
                        mPresenter?.updateMerchantGoodsState(bean.goods_id, 2)
                    }
                    R.id.tv_top -> {
                        mPresenter?.updateMerchantGoodsState(bean.goods_id, 3)
                    }
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getAuctionMerchantGoodsList(mState, mPage, mCount)
    }

    override fun createPresenter() = ManagerAuctionPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getAuctionMerchantGoodsListSuccess(bean: MerchantGoodsBean) {
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

    override fun getDraftAuctionGoodsListSuccess() {


    }

    override fun updateMerchantGoodsStateSuccess(state: Int) {
        reload()
    }

}
