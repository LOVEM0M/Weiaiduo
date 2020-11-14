package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantPayList

class IncomeDetailContract {

    interface IView : IBaseView {
        fun getWallerMerchantPayListSuccess(bean: MerchantPayList)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getWallerMerchantPayList(changeType: Int, currentPage: Int, pageSize: Int, tag: Int)
    }

}
