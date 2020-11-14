package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class SubAccountSettingContract {

    interface IView : IBaseView {
        fun onAddSubAccountSuccess()

        fun onUpdateSubAccountSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun addSubAccount(type: Int, avatar: String?, name: String?, account: String?, password: String?)

        fun updateSubAccount(id: Int, avatar: String?, name: String?, account: String?, password: String?)
    }

}