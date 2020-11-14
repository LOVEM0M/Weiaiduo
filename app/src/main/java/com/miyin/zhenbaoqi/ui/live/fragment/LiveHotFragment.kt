package com.miyin.zhenbaoqi.ui.live.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.LiveHotBean
import com.miyin.zhenbaoqi.ui.live.adapter.LiveHotAdapter
import com.miyin.zhenbaoqi.ui.live.contract.LiveHotContract
import com.miyin.zhenbaoqi.ui.live.presenter.LiveHotPresenter
import kotlinx.android.synthetic.main.fragment_live_goods.recycler_view
import kotlinx.android.synthetic.main.fragment_live_goods.smart_refresh_layout

class LiveHotFragment : BaseListFragment<LiveHotContract.IView, LiveHotContract.IPresenter>(), LiveHotContract.IView {

    private var mList = mutableListOf<LiveHotBean.LiveHotDataBean>()
    private lateinit var mAdapter: LiveHotAdapter

    companion object {
        fun newInstance() = LiveHotFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
        }
        return R.layout.fragment_live_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = LiveHotAdapter(mList)
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
        mPresenter?.obtainHotList(mPage, mCount)
    }

    override fun createPresenter() = LiveHotPresenter()

    override fun obtainHotListSuccess(data: LiveHotBean) {
        with(data) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (!hasMore) {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

    override fun showEmptyView() {
        val emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, recycler_view, false)
        mAdapter.emptyView = emptyView
        smart_refresh_layout.setEnableLoadMore(false)
    }

}
