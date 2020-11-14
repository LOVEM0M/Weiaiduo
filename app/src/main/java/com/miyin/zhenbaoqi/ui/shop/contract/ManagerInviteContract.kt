package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.InvitePlayerBean

class ManagerInviteContract {

    interface IView : IBaseView {
        fun getInviteListSuccess(bean: InvitePlayerBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun inviteExclusiveFansList(currentPage: Int, pageSize: Int)

        fun inviteOrdinaryFansList(currentPage: Int, pageSize: Int)

        fun invitePlayerList(currentPage: Int, pageSize: Int)
    }

}
