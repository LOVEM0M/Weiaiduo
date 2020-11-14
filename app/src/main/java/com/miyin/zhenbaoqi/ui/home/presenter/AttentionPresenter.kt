package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.HomeMerchantBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.AttentionContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AttentionPresenter : BasePresenter<AttentionContract.IView>(), AttentionContract.IPresenter {

    override fun getMerchantList() {
        request(RetrofitUtils.mApiService.homeMerchant(), object : BaseSingleObserver<HomeMerchantBean>() {
            override fun doOnSuccess(data: HomeMerchantBean) {
                getView()?.showNormal()
                getView()?.getMerchantListSuccess(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

    override fun updateMerchantState(state: Int, merchantId: Int, position: Int) {
        val status = if (state == 0) 1 else 0
        val keyArray = arrayOf("focus_state", "merchant_id")
        val valueArray = arrayOf<Any>(status, merchantId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAttention(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateMerchantStateSuccess(status, position)
            }
        })
    }

}
