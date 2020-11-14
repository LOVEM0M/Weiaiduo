package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerVideoContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ManagerVideoPresenter : BasePresenter<ManagerVideoContract.IView>(), ManagerVideoContract.IPresenter {

    override fun getVideoList(currentPage: Int, pageSize: Int, type: Int) {
        val keyArray = arrayOf("current_page", "page_size", "state")
        val valueArray = arrayOf<Any>(currentPage, pageSize, type)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantVideoList(requestBody), object : BaseSingleObserver<HomeVideoBean>() {
            override fun doOnSuccess(data: HomeVideoBean) {
                getView()?.showNormal()
                getView()?.getVideoListSuccess(data)
            }

            override fun doOnFailure(data: HomeVideoBean) {
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

    override fun deleteVideo(videoId: Int) {
        request(RetrofitUtils.mApiService.merchantVideoDelete(videoId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.deleteVideoSuccess()
            }
        })
    }

}
