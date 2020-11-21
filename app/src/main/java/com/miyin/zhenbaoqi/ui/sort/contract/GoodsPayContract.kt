package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*

class GoodsPayContract {

    interface IView : IBaseView {
        fun getCouponAvailableListSuccess(bean: CouponBean)

        fun getAddressListSuccess(bean: AddressBean)

        fun getAmountRuleSuccess(bean: ServiceAmountBean)

        fun placeOrderSuccess(bean: placeOrderBean, payType: Int)

        fun onFailure(flag: Int)
    }

    interface IPresent : IBasePresenter<IView> {
        fun getCouponAvailableList(goodsId: Int)

        fun getAddressList()

        fun getAmountRule()

        fun placeOrder(adsId: Int,  goodsId: Int, payNumber: Int , payType: Int)

    }

}