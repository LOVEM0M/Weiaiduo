package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantPayList
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.IncomeDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class IncomeDetailPresenter : BasePresenter<IncomeDetailContract.IView>(), IncomeDetailContract.IPresenter {

    override fun getWallerMerchantPayList(changeType: Int, currentPage: Int, pageSize: Int, tag: Int) {
        val keyArray = arrayOf("change_type", "current_page", "page_size")
        val valueArray = arrayOf<Any>(changeType, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)

        val observable = if (tag == 0) {
            RetrofitUtils.mApiService.walletMerchantEarnList(requestBody)
        } else {
            RetrofitUtils.mApiService.walletMerchantPayList(requestBody)
        }
        request(observable, object : BaseSingleObserver<MerchantPayList>() {
            override fun doOnSuccess(data: MerchantPayList) {
                getView()?.showNormal()
                getView()?.getWallerMerchantPayListSuccess(data)
            }

            override fun doOnFailure(data: MerchantPayList) {
                if (currentPage == 1 && data.mark == "1") {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.tip)
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
