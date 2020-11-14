package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantTotalIncomeBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.TotalIncomeContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class TotalIncomePresenter : BasePresenter<TotalIncomeContract.IView>(), TotalIncomeContract.IPresenter {

    override fun getTotalIncome(type: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("type"), arrayOf(type))
        request(RetrofitUtils.mApiService.walletMerchantTotalIncome(requestBody), object : BaseSingleObserver<MerchantTotalIncomeBean>() {
            override fun doOnSuccess(data: MerchantTotalIncomeBean) {
                getView()?.getTotalIncomeSuccess(data)
            }
        })
    }

}