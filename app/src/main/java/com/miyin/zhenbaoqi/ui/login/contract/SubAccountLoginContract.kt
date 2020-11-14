package com.miyin.zhenbaoqi.ui.login.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.SubAccountLoginBean

class SubAccountLoginContract {

    interface IView : IBaseView {
        fun loginSuccess(bean: SubAccountLoginBean.DataBean.RoomBean?)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun login(account: String?, password: String?)
    }

}
