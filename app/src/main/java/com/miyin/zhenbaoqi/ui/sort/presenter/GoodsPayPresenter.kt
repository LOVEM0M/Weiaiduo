package com.miyin.zhenbaoqi.ui.sort.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsPayContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class GoodsPayPresenter : BasePresenter<GoodsPayContract.IView>(), GoodsPayContract.IPresent {

    override fun getCouponAvailableList(goodsId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("goods_id"), arrayOf(goodsId))
        request(RetrofitUtils.mApiService.couponAvailableList(requestBody), object : BaseSingleObserver<CouponBean>() {
            override fun doOnSuccess(data: CouponBean) {
                getView()?.getCouponAvailableListSuccess(data)
            }

            override fun doOnFailure(data: CouponBean) {
                getView()?.onFailure(1)
            }
        })
    }

    override fun getAddressList() {
        val requestBody = JSONUtils.createJSON(arrayOf("current_page", "page_size"), arrayOf(1, 100))
        request(RetrofitUtils.mApiService.addressList(requestBody), object : BaseSingleObserver<AddressBean>() {
            override fun doOnSuccess(data: AddressBean) {
                getView()?.getAddressListSuccess(data)
            }

            override fun doOnFailure(data: AddressBean) {
                getView()?.onFailure(0)
            }
        })
    }

    override fun getAmountRule() {
        request(RetrofitUtils.mApiService.goodsAmountRule(), object : BaseSingleObserver<ServiceAmountBean>() {
            override fun doOnSuccess(data: ServiceAmountBean) {
                getView()?.getAmountRuleSuccess(data)
            }
        })
    }

    override fun goodsPay(adsId: Int, couponsId: Int, goodsId: Int, payType: Int, remark: String?, treasureAmount: Int, payNumber: Int) {
        val keyArray = arrayOf("ads_id", "coupons_id", "goods_id", "pay_type", "remark", "treasure_amount", "pay_number")
        val valueArray = arrayOf<Any>(adsId, couponsId, goodsId, payType, remark ?: "", treasureAmount, payNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.orderPay(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.goodsPaySuccess(data, payType)
            }
        })
    }

    override fun auctionGoodsPay(adsId: Int, couponsId: Int, goodsId: Int, payType: Int, remark: String?, treasureAmount: Int) {
        val keyArray = arrayOf("ads_id", "coupons_id", "goods_id", "pay_type", "remark", "treasure_amount")
        val valueArray = arrayOf<Any>(adsId, couponsId, goodsId, payType, remark
                ?: "", treasureAmount)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.auctionOrderInsert(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.goodsPaySuccess(data, payType)
            }
        })
    }

    override fun auctionGoodsWaitPay(orderNumber: String, payType: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.waitOrderPay(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.goodsPaySuccess(data, payType)
            }
        })
    }

}