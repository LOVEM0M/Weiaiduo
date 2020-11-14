package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.BecomeMerchantContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BecomeMerchantPresenter : BasePresenter<BecomeMerchantContract.IView>(), BecomeMerchantContract.IPresenter {

    override fun applyMerchant(payType: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("pay_type"), arrayOf(payType))
        request(RetrofitUtils.mApiService.applyMerchant(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.applyMerchantSuccess(data, payType)
            }

            override fun doOnFailure(data: PayResultBean) {
                if (data.msg == "需要实名认证" && data.code == 502) {
                    getView()?.onFailure()
                } else {
                    super.doOnFailure(data)
                }
            }
        })
    }

}