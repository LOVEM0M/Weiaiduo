package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.bean.LiveHotBean
import com.miyin.zhenbaoqi.bean.LiveShareBean
import com.miyin.zhenbaoqi.bean.ResponseBean

class LiveShareContract {

    interface IView : IBaseView {
        fun obtainShareListSuccess(data: LiveShareBean)
        fun showEmptyView()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun obtainSharedList(currentPage: Int, pageSize: Int)
    }

}
