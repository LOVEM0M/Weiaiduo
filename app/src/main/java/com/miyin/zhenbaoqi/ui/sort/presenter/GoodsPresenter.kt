package com.miyin.zhenbaoqi.ui.sort.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.NewGoodsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class GoodsPresenter : BasePresenter<GoodsContract.IView>(), GoodsContract.IPresenter {

    override fun getNewGoodsList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.newGoodsList(requestBody), object : BaseSingleObserver<NewGoodsBean>() {
            override fun doOnSuccess(data: NewGoodsBean) {
                getView()?.showNormal()
                getView()?.getNewGoodsListSuccess(data)
            }

            override fun doOnFailure(data: NewGoodsBean) {
                if (currentPage == 1) {
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
