package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ImageBean

class SetWeChatContract {

    interface IView : IBaseView {
        fun uploadImageSuccess(bean: ImageBean)

        fun inviteMerchantWeChatSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadImage(path: String)

        fun inviteMerchantWeChat(weChatId: String, weChatImage: String)
    }

}