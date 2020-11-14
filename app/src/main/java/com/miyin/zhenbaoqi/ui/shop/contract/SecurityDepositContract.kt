package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.bean.TotalAmountBean

class SecurityDepositContract {

    interface IView : IBaseView {
        fun addQualityMoneySuccess(bean: PayResultBean, payType: Int)

        fun getTotalAmountSuccess(bean: TotalAmountBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun addQualityMoney(amount: Long, payType: Int)

        fun getTotalAmount()
    }

}