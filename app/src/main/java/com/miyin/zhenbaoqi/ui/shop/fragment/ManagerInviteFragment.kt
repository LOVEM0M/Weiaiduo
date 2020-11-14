package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.InvitePlayerBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.activity.invite.FanDetailActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ManagerInviteAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerInviteContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ManagerInvitePresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class ManagerInviteFragment : BaseListFragment<ManagerInviteContract.IView, ManagerInviteContract.IPresenter>(), ManagerInviteContract.IView {

    private var mList = mutableListOf<InvitePlayerBean.DataBean>()
    private lateinit var mAdapter: ManagerInviteAdapter
    private var mTitle: String? = null

    companion object {
        fun newInstance(title: String) = ManagerInviteFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mTitle = getString("title")
        }
        return R.layout.fragment_manager_invite
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ManagerInviteAdapter(mList, mTitle)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { _, _, position ->
                if (mTitle == "普通粉丝") {
                    startActivity<FanDetailActivity>("bean" to mList[position])
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        when (mTitle) {
            "新秀" -> mPresenter?.invitePlayerList(mPage, mCount)
            "专属粉丝" -> mPresenter?.inviteExclusiveFansList(mPage, mCount)
            else -> mPresenter?.inviteOrdinaryFansList(mPage, mCount)
        }
    }

    override fun createPresenter() = ManagerInvitePresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getInviteListSuccess(bean: InvitePlayerBean) {
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

}
