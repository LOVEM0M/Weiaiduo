package com.miyin.zhenbaoqi.ui.mine.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.OrderBean
import com.miyin.zhenbaoqi.bean.OrderCancelBean
import com.miyin.zhenbaoqi.bean.OrderConfirmReceiveBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.OrderContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class OrderPresenter : BasePresenter<OrderContract.IView>(), OrderContract.IPresenter {

    override fun getOrderList(page: Int, rows: Int, state: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
            put("state", state)
        }
        request(RetrofitUtils.mApiService.orderList(map), object : BaseSingleObserver<OrderBean>() {
            override fun doOnSuccess(data: OrderBean) {
                getView()?.showNormal()
                getView()?.getOrderListSuccess(data)
            }

            override fun doOnFailure(data: OrderBean) {
                if ( data.data?.total== 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }

    override fun confirmReceive(orderNumber: String) {
        val map = ArrayMap<String, Any>().apply {
            put("orderNumber", orderNumber)
        }
        request(RetrofitUtils.mApiService.orderConfirmReceive(map), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.confirmReceiveSuccess()
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

    override fun deleteOrder(orderNumber: String) {
        if (orderNumber.isEmpty()) {
            getView()?.showToast("订单编号不能为空")
            return
        }
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.deleteOrder(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.orderDeleteSuccess()
            }
        })
    }

    override fun searchOrder(currentPage: Int, pageSize: Int, keyWord: String) {
        val keyArray = arrayOf("current_page", "page_size", "keyWord")
        val valueArray = arrayOf<Any>(currentPage, pageSize, keyWord)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.searchOrderList(requestBody), object : BaseSingleObserver<OrderBean>() {
            override fun doOnSuccess(data: OrderBean) {
                getView()?.showNormal()
                getView()?.getOrderListSuccess(data)
            }

            override fun doOnFailure(data: OrderBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }

}
