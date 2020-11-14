package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AfterSaleStateContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AfterSaleStatePresenter : BasePresenter<AfterSaleStateContract.IView>(), AfterSaleStateContract.IPresenter {

    override fun getAfterSaleDetail(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.afterSaleDetail(requestBody), object : BaseSingleObserver<AfterSaleDetailBean>() {
            override fun doOnSuccess(data: AfterSaleDetailBean) {
                getView()?.getAfterSaleDetailSuccess(data)
            }
        })
    }

    override fun orderAfterCancel(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber!!))
        request(RetrofitUtils.mApiService.afterSaleCancel(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.orderAfterCancelSuccess()
            }
        })
    }

    override fun confirmShip(courierNo: String?, dictId: String?, orderNumber: String) {
        if (dictId.isNullOrEmpty()) {
            getView()?.showToast("请选择快递公司")
            return
        }
        if (courierNo.isNullOrEmpty()) {
            getView()?.showToast("请输入或识别快递单号")
            return
        }

        val keyArray = arrayOf("courier_no", "dict_id", "order_number")
        val valueArray = arrayOf<Any>(courierNo!!, dictId!!, orderNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.afterSaleShip(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.confirmShipSuccess()
            }
        })
    }

    override fun afterSaleLaunch(afterImg: String, afterType: Int, afterWhy: String?, orderNumber: String) {
        if (afterWhy.isNullOrEmpty()) {
            getView()?.showToast("请选择售后原因")
            return
        }

        val keyArray = arrayOf("after_img", "after_type", "after_why", "order_number")
        val valueArray = arrayOf(afterImg, afterType, afterWhy!!, orderNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.afterSaleLaunch(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.afterSaleLaunchSuccess()
            }
        })
    }

}