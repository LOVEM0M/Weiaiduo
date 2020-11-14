package com.miyin.zhenbaoqi.ui.message.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.message.adapter.IncomeAdapter
import com.miyin.zhenbaoqi.ui.message.contract.MessageContract
import com.miyin.zhenbaoqi.ui.message.presenter.MessagePresenter
import com.miyin.zhenbaoqi.ui.mine.activity.wallet.WalletActivity
import com.miyin.zhenbaoqi.ui.shop.activity.purse.PurseActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class IncomeActivity : BaseListActivity<MessageContract.IView, MessageContract.IPresenter>(), MessageContract.IView {

    private var mList = mutableListOf<MessageBean.ListBean>()
    private lateinit var mAdapter: IncomeAdapter
    private var mMerchantId = 0

    override fun getContentView() = R.layout.activity_income

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("收益通知")
        immersionBar { statusBarDarkFont(true) }

        showLoading()
        recycler_view.run {
            mAdapter = IncomeAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)

            mAdapter.setOnItemClickListener { _, _, _ ->
                if (mMerchantId == 0) {
                    startActivity<WalletActivity>()
                } else {
                    startActivity<PurseActivity>()
                }
            }
        }
    }

    override fun initData() {
        mPresenter?.getMessageList(2, mPage, mCount)
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
        mMerchantId = bean.merchants_id
    }

}
