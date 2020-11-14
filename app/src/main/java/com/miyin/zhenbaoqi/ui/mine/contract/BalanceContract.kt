package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.TotalAmountBean

class BalanceContract {

    interface IView : IBaseView {
        fun getBalanceSuccess(balance: Float)

        fun getTotalAmountSuccess(bean: TotalAmountBean)

        fun merchantHasAuthSuccess(state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBalance()

        fun getTotalAmount()

        fun merchantHasAuth()
    }

}