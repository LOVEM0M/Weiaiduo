package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CollectBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.CollectContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class CollectPresenter : BasePresenter<CollectContract.IView>(), CollectContract.IPresenter {

    override fun getCollectList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.goodsCollectList(requestBody), object : BaseSingleObserver<CollectBean>() {
            override fun doOnSuccess(data: CollectBean) {
                getView()?.showNormal()
                getView()?.getCollectListSuccess(data)
            }

            override fun doOnFailure(data: CollectBean) {
                if (data.code == 1) {
                    if (currentPage == 1) {
                        getView()?.showEmpty()
                    } else {
                        getView()?.showToast(data.msg)
                    }
                } else {
                    getView()?.showToast(data.msg)
                    getView()?.showError()
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
