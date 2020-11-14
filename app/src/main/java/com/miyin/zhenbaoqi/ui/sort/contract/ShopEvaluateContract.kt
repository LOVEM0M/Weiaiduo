package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantEvalBean

class ShopEvaluateContract {

    interface IView : IBaseView {
        fun getShopEvalListSuccess(bean: MerchantEvalBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getShopEvalList(merchantId: Int)
    }

}
