package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.OrderDetailBean
import com.miyin.zhenbaoqi.bean.PayResultBean

class OrderDetailContract {

    interface IView : IBaseView {
        fun getOrderDetailSuccess(bean: OrderDetailBean)

        fun orderCancelSuccess()

        fun confirmReceiveSuccess()

        fun orderPaySuccess(bean: PayResultBean, payType: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getOrderDetail(orderNumber: String)

        fun orderCancel(orderNumber: String)

        fun confirmReceive(orderNumber: String)

        fun orderPay(orderNumber: String, payType: Int)
    }

}
