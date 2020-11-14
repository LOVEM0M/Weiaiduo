package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.OrderBean

class OrderContract {

    interface IView : IBaseView {
        fun getOrderListSuccess(bean: OrderBean)

        fun confirmReceiveSuccess()

        fun orderCancelSuccess()

        fun orderDeleteSuccess()

        fun searchOrderSuccess(bean: OrderBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getOrderList(currentPage: Int, pageSize: Int, state: Int)

        fun confirmReceive(orderNumber: String)

        fun orderCancel(orderNumber: String)

        fun deleteOrder(orderNumber: String)

        fun searchOrder(currentPage: Int, pageSize: Int, keyWord: String)
    }

}