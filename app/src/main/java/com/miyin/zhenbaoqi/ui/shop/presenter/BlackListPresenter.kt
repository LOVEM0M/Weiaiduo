package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BlackListBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.BlackListContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BlackListPresenter : BasePresenter<BlackListContract.IView>(), BlackListContract.IPresenter {

    override fun getBlackList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantBlackList(requestBody), object : BaseSingleObserver<BlackListBean>() {
            override fun doOnSuccess(data: BlackListBean) {
                getView()?.showNormal()
                getView()?.getBlackListSuccess(data)
            }

            override fun doOnFailure(data: BlackListBean) {
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

    override fun removeBlackList(userId: Int) {
        request(RetrofitUtils.mApiService.merchantRemoveBlackList(userId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.removeBlackListSuccess()
            }
        })
    }

}
