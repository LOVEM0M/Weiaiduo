package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.bean.LiveHotBean

class LiveHotContract {

    interface IView : IBaseView {
        fun obtainHotListSuccess(data: LiveHotBean)
        fun showEmptyView()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun obtainHotList(currentPage: Int, pageSize: Int)
    }

}
