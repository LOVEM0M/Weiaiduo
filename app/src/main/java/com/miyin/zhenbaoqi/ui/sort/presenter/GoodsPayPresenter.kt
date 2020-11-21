package com.miyin.zhenbaoqi.ui.sort.presenter

import androidx.collection.ArrayMap
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
        val map = ArrayMap<String, Any>().apply {
            put("page", 1)
            put("rows", 100)
        }
        request(RetrofitUtils.mApiService.addressList(map), object : BaseSingleObserver<AddressBean>() {
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

    override fun placeOrder(adsId: Int, goodsId: Int, payNumber: Int, payType: Int) {
        val keyArray = arrayOf("adsId", "goodsId", "payNumber", "payType")
        val valueArray = arrayOf<Any>(adsId, goodsId, payNumber, 1)//暂时固定写1，2和3还没做

        val requestBody = JSONUtils.createJSON2(keyArray, valueArray)

        request(RetrofitUtils.mApiService.placeOrder(requestBody), object : BaseSingleObserver<placeOrderBean>() {
            override fun doOnSuccess(data: placeOrderBean) {
                getView()?.placeOrderSuccess(data, payType)
            }

            override fun doOnFailure(data: placeOrderBean) {
                getView()?.showToast(data?.msg)
                super.doOnFailure(data)
            }

            override fun onError(e: Throwable) {
                getView()?.showToast(e.message)
                super.onError(e)
            }
        })
    }





}
