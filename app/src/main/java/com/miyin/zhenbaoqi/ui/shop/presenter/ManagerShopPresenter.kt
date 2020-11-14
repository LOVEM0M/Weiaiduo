package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerShopContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ManagerShopPresenter : BasePresenter<ManagerShopContract.IView>(), ManagerShopContract.IPresenter {

    override fun getMerchantGoodsList(currentPage: Int, pageSize: Int, state: Int) {
        val keyArray = arrayOf("current_page", "page_size", "state")
        val valueArray = arrayOf<Any>(currentPage, pageSize, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantGoodsList(requestBody), object : BaseSingleObserver<MerchantGoodsBean>() {
            override fun doOnSuccess(data: MerchantGoodsBean) {
                getView()?.showNormal()
                getView()?.getMerchantGoodsListSuccess(data)
            }

            override fun doOnFailure(data: MerchantGoodsBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
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

    override fun updateMerchantGoodsState(goodsId: Int, state: Int) {
        val keyArray = arrayOf("goods_id", "state")
        val valueArray = arrayOf<Any>(goodsId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateMerchantGoodsState(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateMerchantGoodsStateSuccess(state)
            }
        })
    }

}
