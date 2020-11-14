package com.miyin.zhenbaoqi.ui.message.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.ui.message.adapter.TransactionLogisticsAdapter
import com.miyin.zhenbaoqi.ui.message.contract.MessageContract
import com.miyin.zhenbaoqi.ui.message.presenter.MessagePresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class TransactionLogisticsActivity : BaseListActivity<MessageContract.IView, MessageContract.IPresenter>(), MessageContract.IView {

    private lateinit var mAdapter: TransactionLogisticsAdapter
    private var mList = mutableListOf<MessageBean.ListBean>()

    override fun getContentView(): Int {
        return R.layout.activity_transaction_logistics
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("交易物流")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            mAdapter = TransactionLogisticsAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getMessageList(1, mPage, mCount)
    }

    override fun createPresenter() = MessagePresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getMessageListSuccess(bean: MessageBean) {
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

    override fun notReadMessageSize(bean: MessageBean) {

    }

    override fun searchMerchantIdSuccess(bean: UserTypeBean) {

    }

}
