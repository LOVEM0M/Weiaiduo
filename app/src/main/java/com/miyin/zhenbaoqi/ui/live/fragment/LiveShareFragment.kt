package com.miyin.zhenbaoqi.ui.live.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.LiveShareBean
import com.miyin.zhenbaoqi.ui.live.adapter.LeaderBoardAdapter
import com.miyin.zhenbaoqi.ui.live.contract.LiveShareContract
import com.miyin.zhenbaoqi.ui.live.presenter.LiveSharePresenter
import kotlinx.android.synthetic.main.fragment_live_goods.*

class LiveShareFragment : BaseListFragment<LiveShareContract.IView, LiveShareContract.IPresenter>(), LiveShareContract.IView {

    private var mList = mutableListOf<LiveShareBean.LiveShareListBean>()
    private lateinit var mAdapter: LeaderBoardAdapter

    companion object {
        fun newInstance() = LiveShareFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
        }
        return R.layout.fragment_live_share
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = LeaderBoardAdapter(mList)
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
        mPresenter?.obtainSharedList(mPage, mCount)
    }

    override fun createPresenter() = LiveSharePresenter()

    override fun obtainShareListSuccess(data: LiveShareBean) {
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
