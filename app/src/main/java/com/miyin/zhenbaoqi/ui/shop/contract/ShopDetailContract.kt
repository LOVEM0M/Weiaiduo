package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantGoodsStoreBean

class ShopDetailContract {

    interface IView : IBaseView {
        fun getMerchantGoodsListSuccess(bean: MerchantGoodsStoreBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantGoodsList(currentPage: Int, merchantId: Int, pageSize: Int, type: Int)
    }

}