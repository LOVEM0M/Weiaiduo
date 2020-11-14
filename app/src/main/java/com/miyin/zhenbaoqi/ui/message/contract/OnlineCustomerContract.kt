package com.miyin.zhenbaoqi.ui.message.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.UserTypeBean

class OnlineCustomerContract {

    interface IView : IBaseView {
        fun getMerchantIdSuccess(bean: UserTypeBean)

        fun addBlackListSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantId(userId: Int)

        fun getSubMerchantId(accountName: String)

        fun addBlackList(userId: Int)
    }

}
