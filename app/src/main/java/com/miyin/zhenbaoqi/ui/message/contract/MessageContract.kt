package com.miyin.zhenbaoqi.ui.message.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean

class MessageContract {

    interface IView : IBaseView {
        fun getMessageListSuccess(bean: MessageBean)

        fun notReadMessageSize(bean: MessageBean)

        fun searchMerchantIdSuccess(bean: UserTypeBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun searchMerchantId(userId: Int)

        fun getMessageList(messageType: Int, currentPage: Int, pageSize: Int)

        fun notReadMessageSize()
    }

}
