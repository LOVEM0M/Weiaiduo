package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantEarnDetail
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.EarnDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class EarnDetailPresenter : BasePresenter<EarnDetailContract.IView>(), EarnDetailContract.IPresenter {

    override fun getEarnDetail(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            getView()?.showToast("订单编号不能为空")
            return
        }

        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.walletMerchantEarnDetail(requestBody), object : BaseSingleObserver<MerchantEarnDetail>() {
            override fun doOnSuccess(data: MerchantEarnDetail) {
                getView()?.getEarnDetailSuccess(data)
            }
        })
    }

    override fun getPaymentDetail(orderNumber: String?) {
        if (orderNumber.isNullOrEmpty()) {
            getView()?.showToast("订单编号不能为空")
            return
        }

        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.walletMerchantDetail(requestBody), object : BaseSingleObserver<MerchantEarnDetail>() {
            override fun doOnSuccess(data: MerchantEarnDetail) {
                getView()?.getEarnDetailSuccess(data)
            }
        })
    }

}
