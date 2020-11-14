package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BlackListBean

class BlackListContract {

    interface IView : IBaseView {
        fun getBlackListSuccess(bean: BlackListBean)

        fun removeBlackListSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBlackList(currentPage: Int, pageSize: Int)

        fun removeBlackList(userId: Int)
    }

}