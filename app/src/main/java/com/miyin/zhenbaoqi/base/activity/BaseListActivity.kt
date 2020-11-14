package com.miyin.zhenbaoqi.base.activity

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.constant.Constant
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

abstract class BaseListActivity<in V : IBaseView, P : IBasePresenter<V>> : BaseStateActivity<V, P>() {

    protected var mPage = 1
    protected var mCount = 10

    protected fun refreshAndLoadMore(refreshLayout: SmartRefreshLayout, callback: () -> Unit) {
        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                callback()
                refreshLayout.finishLoadMore(Constant.DELAY_TIME)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                callback()
                refreshLayout.finishRefresh(Constant.DELAY_TIME)
            }
        })
    }

    protected fun refresh(refreshLayout: SmartRefreshLayout, callback: () -> Unit) {
        refreshLayout.setOnRefreshListener {
            mPage = 1
            callback()
            it.finishRefresh(Constant.DELAY_TIME)
        }
    }

}
