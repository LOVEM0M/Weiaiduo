package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.PayResultBean

class BecomeMerchantContract {

    interface IView : IBaseView {
        fun applyMerchantSuccess(bean: PayResultBean, payType: Int)

        fun onFailure()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun applyMerchant(payType: Int)
    }

}
