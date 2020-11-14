package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.OrderLogisticsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.LogisticsContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class LogisticsPresenter : BasePresenter<LogisticsContract.IView>(), LogisticsContract.IPresenter {

    override fun getOrderLogistics(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.orderLogistics(requestBody), object : BaseSingleObserver<OrderLogisticsBean>() {
            override fun doOnSuccess(data: OrderLogisticsBean) {
                getView()?.showNormal()
                getView()?.getOrderLogisticsSuccess(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

    override fun getOrderAfterLogistics(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.afterOrderCourier(requestBody), object : BaseSingleObserver<OrderLogisticsBean>() {
            override fun doOnSuccess(data: OrderLogisticsBean) {
                getView()?.showNormal()
                getView()?.getOrderLogisticsSuccess(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

}
