package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.bean.MerchantOrderDetailBean

class ShopOrderDetailContract {

    interface IView : IBaseView {
        fun getOrderDetailSuccess(bean: MerchantOrderDetailBean)

        fun afterSaleDetailSuccess(bean: AfterSaleDetailBean)

        fun orderAfterAuditSuccess()

        fun orderAfterReceiveSuccess()

        fun merchantAgreeFundSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getOrderDetail(orderNumber: String?)

        fun afterSaleDetail(orderNumber: String?)

        fun orderAfterAudit(afterState: Int, orderNumber: String?, refuseReason: String?)

        fun orderAfterReceive(afterState: Int, orderNumber: String?)

        fun merchantAgreeFund(orderNumber: String?)
    }

}
