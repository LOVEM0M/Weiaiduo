package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.bean.MerchantOrderDetailBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ShopOrderDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ShopOrderDetailPresenter : BasePresenter<ShopOrderDetailContract.IView>(), ShopOrderDetailContract.IPresenter {

    override fun getOrderDetail(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber!!))
        request(RetrofitUtils.mApiService.merchantOrderDetail(requestBody), object : BaseSingleObserver<MerchantOrderDetailBean>() {
            override fun doOnSuccess(data: MerchantOrderDetailBean) {
                getView()?.getOrderDetailSuccess(data)
            }
        })
    }

    override fun afterSaleDetail(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber!!))
        request(RetrofitUtils.mApiService.afterSaleDetail(requestBody), object : BaseSingleObserver<AfterSaleDetailBean>() {
            override fun doOnSuccess(data: AfterSaleDetailBean) {
                getView()?.afterSaleDetailSuccess(data)
            }
        })
    }

    override fun orderAfterAudit(afterState: Int, orderNumber: String?, refuseReason: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }
        if (afterState == 3) {
            if (refuseReason.isNullOrEmpty()) {
                getView()?.showToast("请填写拒绝原因")
                return
            }
        }

        val keyArray = arrayOf("after_state", "order_number", "refuse_reason")
        val valueArray = arrayOf<Any>(afterState, orderNumber!!, refuseReason ?: "")
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAfterSaleVerify(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.orderAfterAuditSuccess()
            }
        })
    }

    override fun orderAfterReceive(afterState: Int, orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }

        val keyArray = arrayOf("after_state", "order_number")
        val valueArray = arrayOf<Any>(afterState, orderNumber!!)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantReceive(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.orderAfterReceiveSuccess()
            }
        })
    }

    override fun merchantAgreeFund(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            return
        }

        request(RetrofitUtils.mApiService.merchantAgreeFund(orderNumber!!), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.merchantAgreeFundSuccess()
            }
        })
    }

}
