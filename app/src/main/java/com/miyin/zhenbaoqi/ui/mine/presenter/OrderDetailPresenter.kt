package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.OrderCancelBean
import com.miyin.zhenbaoqi.bean.OrderConfirmReceiveBean
import com.miyin.zhenbaoqi.bean.OrderDetailBean
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.OrderDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class OrderDetailPresenter : BasePresenter<OrderDetailContract.IView>(), OrderDetailContract.IPresenter {

    override fun getOrderDetail(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.orderDetail(requestBody), object : BaseSingleObserver<OrderDetailBean>() {
            override fun doOnSuccess(data: OrderDetailBean) {
                getView()?.getOrderDetailSuccess(data)
            }
        })
    }

    override fun orderCancel(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.orderCancel(requestBody), object : BaseSingleObserver<OrderCancelBean>() {
            override fun doOnSuccess(data: OrderCancelBean) {
                getView()?.orderCancelSuccess()
            }
        })
    }

    override fun confirmReceive(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.orderConfirmReceive(requestBody), object : BaseSingleObserver<OrderConfirmReceiveBean>() {
            override fun doOnSuccess(data: OrderConfirmReceiveBean) {
                getView()?.confirmReceiveSuccess()
            }
        })
    }

    override fun orderPay(orderNumber: String, payType: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.waitOrderPay(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.orderPaySuccess(data, payType)
            }
        })
    }

}
