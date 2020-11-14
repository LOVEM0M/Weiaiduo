package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.SearchShopBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.SearchShopContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SearchShopPresenter : BasePresenter<SearchShopContract.IView>(), SearchShopContract.IPresenter {

    override fun searchShop(currentPage: Int, pageSize: Int, searchParam: String) {
        val keyArray = arrayOf("current_page", "page_size", "search_param")
        val valueArray = arrayOf(currentPage, pageSize, searchParam)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.searchMerchant(requestBody), object : BaseSingleObserver<SearchShopBean>() {
            override fun doOnSuccess(data: SearchShopBean) {
                getView()?.showNormal()
                getView()?.searchShopSuccess(data)
            }

            override fun doOnFailure(data: SearchShopBean) {
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