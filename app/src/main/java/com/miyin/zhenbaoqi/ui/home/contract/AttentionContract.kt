package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.HomeMerchantBean

class AttentionContract {

    interface IView : IBaseView {
        fun getMerchantListSuccess(bean:HomeMerchantBean)

        fun updateMerchantStateSuccess(state: Int, position: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantList()

        fun updateMerchantState(state: Int, merchantId: Int, position: Int)
    }

}
