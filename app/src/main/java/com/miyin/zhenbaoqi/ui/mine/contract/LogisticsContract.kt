package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.OrderLogisticsBean

class LogisticsContract {

    interface IView : IBaseView {
        fun getOrderLogisticsSuccess(bean: OrderLogisticsBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getOrderLogistics(orderNumber: String)

        fun getOrderAfterLogistics(orderNumber: String)
    }

}
