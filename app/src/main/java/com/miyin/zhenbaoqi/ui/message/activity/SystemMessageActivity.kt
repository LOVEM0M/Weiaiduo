package com.miyin.zhenbaoqi.ui.message.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.ui.message.adapter.SystemMessageAdapter
import com.miyin.zhenbaoqi.ui.message.contract.MessageContract
import com.miyin.zhenbaoqi.ui.message.presenter.MessagePresenter
import kotlinx.android.synthetic.main.activity_system_message.*

class SystemMessageActivity : BaseListActivity<MessageContract.IView, MessageContract.IPresenter>(), MessageContract.IView {

    private var mList = mutableListOf<MessageBean.ListBean>()
    private lateinit var mAdapter: SystemMessageAdapter

    override fun getContentView() = R.layout.activity_system_message

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("系统消息")
        immersionBar { statusBarDarkFont(true) }

        showLoading()

        recycler_view.run {
            mAdapter = SystemMessageAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getMessageList(3, mPage, mCount)
    }

    override fun createPresenter() = MessagePresenter()

    override fun reload() {
        showLoading()
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
            smart_refresh_layout.setEnableLoadMore(current_page != pages)
        }
    }

    override fun notReadMessageSize(bean: MessageBean) {

    }

    override fun searchMerchantIdSuccess(bean: UserTypeBean) {

    }

}
