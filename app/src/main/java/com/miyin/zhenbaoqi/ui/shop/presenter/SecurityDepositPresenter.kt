package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.SecurityDepositContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SecurityDepositPresenter : BasePresenter<SecurityDepositContract.IView>(), SecurityDepositContract.IPresenter {

    override fun addQualityMoney(amount: Long, payType: Int) {
        val keyArray = arrayOf("amount", "pay_type")
        val valueArray = arrayOf(amount, payType)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAddQualityMoney(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.addQualityMoneySuccess(data, payType)
            }
        })
    }

    override fun getTotalAmount() {
        request(RetrofitUtils.mApiService.walletMerchantTotalAmount(), object : BaseSingleObserver<TotalAmountBean>() {
            override fun doOnSuccess(data: TotalAmountBean) {
                getView()?.getTotalAmountSuccess(data)
            }
        })
    }

}
