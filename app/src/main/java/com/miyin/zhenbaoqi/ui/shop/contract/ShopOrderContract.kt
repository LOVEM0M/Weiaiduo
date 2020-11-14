package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantOrderBean

class ShopOrderContract {

    interface IView : IBaseView {
        fun getOrderListSuccess(bean: MerchantOrderBean)

        fun merchantOrderSearchSuccess(bean: MerchantOrderBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getOrderList(currentPage: Int, pageSize: Int, state: Int)

        fun merchantOrderSearch(currentPage: Int, pageSize: Int, keyWord: String)
    }

}
