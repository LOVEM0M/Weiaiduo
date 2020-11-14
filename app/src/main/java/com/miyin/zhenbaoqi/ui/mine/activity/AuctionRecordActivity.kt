package com.miyin.zhenbaoqi.ui.mine.activity

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.AuctionRecoredBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.AuctionRecordAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.AuctionRecordContract
import com.miyin.zhenbaoqi.ui.mine.presenter.AuctionRecordPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import kotlinx.android.synthetic.main.activity_auction_record.*

class AuctionRecordActivity : BaseListActivity<AuctionRecordContract.IView, AuctionRecordContract.IPresenter>(), AuctionRecordContract.IView {

    private var mList = mutableListOf<AuctionRecoredBean.ListBean>()
    private lateinit var mAdapter: AuctionRecordAdapter

    override fun getContentView() = R.layout.activity_auction_record

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("参与纪录")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            mAdapter = AuctionRecordAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                startActivity<AuctionDetailActivity>("goods_id" to bean.goods_id, "state" to bean.state)
                finish()
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getAuctionRecord(mPage, mCount)
    }

    override fun createPresenter() = AuctionRecordPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getAuctionRecordSuccess(bean: AuctionRecoredBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = pages != current_page
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.addNoMoreDataFooter()
            } else {
                mAdapter.removeAllFooterView()
            }
        }
    }

}
