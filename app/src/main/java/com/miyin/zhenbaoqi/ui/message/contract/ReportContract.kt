package com.miyin.zhenbaoqi.ui.message.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class ReportContract {

    interface IView : IBaseView {
        fun uploadImageSuccess(url: String)

        fun reportSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadImage(path: String)

        fun report(merchantId: Int, content: String?, images: String, cate: String?)
    }

}
