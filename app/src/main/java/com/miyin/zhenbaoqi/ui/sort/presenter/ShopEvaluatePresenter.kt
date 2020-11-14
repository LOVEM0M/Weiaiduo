package com.miyin.zhenbaoqi.ui.sort.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantEvalBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.ShopEvaluateContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ShopEvaluatePresenter : BasePresenter<ShopEvaluateContract.IView>(), ShopEvaluateContract.IPresenter {

    override fun getShopEvalList(merchantId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("merchants_id"), arrayOf(merchantId))
        request(RetrofitUtils.mApiService.merchantEvalList(requestBody), object : BaseSingleObserver<MerchantEvalBean>() {
            override fun doOnSuccess(data: MerchantEvalBean) {
                getView()?.getShopEvalListSuccess(data)
            }

            override fun doOnFailure(data: MerchantEvalBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

}
