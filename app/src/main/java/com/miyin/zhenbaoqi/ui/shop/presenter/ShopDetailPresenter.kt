package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantGoodsStoreBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ShopDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ShopDetailPresenter : BasePresenter<ShopDetailContract.IView>(), ShopDetailContract.IPresenter {

    override fun getMerchantGoodsList(currentPage: Int, merchantId: Int, pageSize: Int, type: Int) {
        val keyArray = arrayOf("current_page", "merchants_id", "page_size", "type")
        val valueArray = arrayOf<Any>(currentPage, merchantId, pageSize, type)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantGoodsStoreList(requestBody), object : BaseSingleObserver<MerchantGoodsStoreBean>() {
            override fun doOnSuccess(data: MerchantGoodsStoreBean) {
                getView()?.showNormal()
                getView()?.getMerchantGoodsListSuccess(data)
            }

            override fun doOnFailure(data: MerchantGoodsStoreBean) {
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