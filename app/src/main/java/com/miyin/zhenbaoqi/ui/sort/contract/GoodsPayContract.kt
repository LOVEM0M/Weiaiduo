package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.bean.ServiceAmountBean

class GoodsPayContract {

    interface IView : IBaseView {
        fun getCouponAvailableListSuccess(bean: CouponBean)

        fun getAddressListSuccess(bean: AddressBean)

        fun getAmountRuleSuccess(bean: ServiceAmountBean)

        fun goodsPaySuccess(bean: PayResultBean, payType: Int)

        fun onFailure(flag: Int)
    }

    interface IPresent : IBasePresenter<IView> {
        fun getCouponAvailableList(goodsId: Int)

        fun getAddressList()

        fun getAmountRule()

        fun goodsPay(adsId: Int, couponsId: Int, goodsId: Int, payType: Int, remark: String?, payNumber: Int)

        fun auctionGoodsPay(adsId: Int, couponsId: Int, goodsId: Int, payType: Int, remark: String?)

        fun auctionGoodsWaitPay(orderNumber: String, payType: Int)
    }

}