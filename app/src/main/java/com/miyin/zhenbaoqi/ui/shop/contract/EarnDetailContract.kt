package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantEarnDetail

class EarnDetailContract {

    interface IView : IBaseView {
        fun getEarnDetailSuccess(bean: MerchantEarnDetail)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getEarnDetail(orderNumber: String?)

        fun getPaymentDetail(orderNumber: String?)
    }

}
