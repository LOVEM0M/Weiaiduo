package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean

class AfterSaleStateContract {

    interface IView : IBaseView {
        fun getAfterSaleDetailSuccess(bean: AfterSaleDetailBean)

        fun orderAfterCancelSuccess()

        fun confirmShipSuccess()

        fun afterSaleLaunchSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAfterSaleDetail(orderNumber: String)

        fun orderAfterCancel(orderNumber: String?)

        fun confirmShip(courierNo: String?, dictId: String?, orderNumber: String)

        fun afterSaleLaunch(afterImg: String, afterType: Int, afterWhy: String?, orderNumber: String)
    }

}