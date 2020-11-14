package com.miyin.zhenbaoqi.ui.message.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.NoticeBean
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.message.adapter.OfficialNotificationAdapter
import com.miyin.zhenbaoqi.ui.message.contract.OfficialNotificationContract
import com.miyin.zhenbaoqi.ui.message.presenter.OfficialNotificationPresenter
import kotlinx.android.synthetic.main.activity_official_notification.*

class OfficialNotificationActivity : BaseListActivity<OfficialNotificationContract.IView, OfficialNotificationContract.IPresenter>(), OfficialNotificationContract.IView {

    private var mList = mutableListOf<NoticeBean.ListBean>()
    private lateinit var mAdapter: OfficialNotificationAdapter

    override fun getContentView() = R.layout.activity_official_notification

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("官方公告")
        immersionBar { statusBarDarkFont(true) }

        showLoading()

        recycler_view.run {
            mAdapter = OfficialNotificationAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)

            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                WebActivity.openActivity(applicationContext, "文章详情", bean.url!!)
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getNoticeList(5, mPage, mCount)
    }

    override fun createPresenter() = OfficialNotificationPresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    override fun getNoticeListSuccess(bean: NoticeBean) {
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
