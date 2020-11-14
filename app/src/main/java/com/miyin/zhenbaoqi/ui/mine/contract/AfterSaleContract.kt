package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class AfterSaleContract {

    interface IView : IBaseView {
        fun uploadImgSuccess(url: String)

        fun afterSaleLaunchSuccess()

        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadImage(path: String)

        fun afterSaleLaunch(afterImg: String, afterType: Int, afterWhy: String?, orderNumber: String)
    }

}
