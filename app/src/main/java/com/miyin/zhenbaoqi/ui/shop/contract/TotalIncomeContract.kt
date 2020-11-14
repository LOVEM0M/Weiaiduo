package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantTotalIncomeBean

class TotalIncomeContract {

    interface IView : IBaseView {
        fun getTotalIncomeSuccess(bean: MerchantTotalIncomeBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getTotalIncome(type: Int)
    }

}
