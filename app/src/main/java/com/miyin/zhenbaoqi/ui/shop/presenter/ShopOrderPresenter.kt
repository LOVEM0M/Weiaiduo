package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantOrderBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ShopOrderContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ShopOrderPresenter : BasePresenter<ShopOrderContract.IView>(), ShopOrderContract.IPresenter {

    override fun getOrderList(currentPage: Int, pageSize: Int, state: Int) {
        val keyArray = arrayOf("state", "current_page", "page_size")
        val valueArray = arrayOf<Any>(state, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantOrderList(requestBody), object : BaseSingleObserver<MerchantOrderBean>() {
            override fun doOnSuccess(data: MerchantOrderBean) {
                getView()?.showNormal()
                getView()?.getOrderListSuccess(data)
            }

            override fun doOnFailure(data: MerchantOrderBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    super.doOnFailure(data)
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

    override fun merchantOrderSearch(currentPage: Int, pageSize: Int, keyWord: String) {
        val keyArray = arrayOf("keyWord", "current_page", "page_size")
        val valueArray = arrayOf<Any>(keyWord, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantOrderSearch(requestBody), object : BaseSingleObserver<MerchantOrderBean>() {
            override fun doOnSuccess(data: MerchantOrderBean) {
                getView()?.showNormal()
                getView()?.getOrderListSuccess(data)
            }

            override fun doOnFailure(data: MerchantOrderBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    super.doOnFailure(data)
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
