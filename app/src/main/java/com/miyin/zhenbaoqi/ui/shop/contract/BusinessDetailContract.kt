package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class BusinessDetailContract {

    interface IView : IBaseView {
        fun joinActivitySuccess()

        fun cancelActivitySuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun joinActivity(activityId: Int)

        fun cancelActivity(activityId: Int)
    }

}
