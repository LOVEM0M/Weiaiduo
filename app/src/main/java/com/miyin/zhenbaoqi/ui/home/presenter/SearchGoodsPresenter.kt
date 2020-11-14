package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.SearchGoodsContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SearchGoodsPresenter : BasePresenter<SearchGoodsContract.IView>(), SearchGoodsContract.IPresenter {

    override fun searchGoods(currentPage: Int, pageSize: Int, param: String, type: Int) {
        val keyArray = arrayOf("current_page", "page_size", "search_param", "type")
        val valueArray = arrayOf(currentPage, pageSize, param, type)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.goodsSearch(requestBody), object : BaseSingleObserver<GoodsSearchBean>() {
            override fun doOnSuccess(data: GoodsSearchBean) {
                getView()?.showNormal()
                getView()?.searchGoodsSuccess(data)
            }

            override fun doOnFailure(data: GoodsSearchBean) {
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