package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.VideoListContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class VideoListPresenter : BasePresenter<VideoListContract.IView>(), VideoListContract.IPresenter {

    override fun getVideoList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.homeVideoList(requestBody), object : BaseSingleObserver<HomeVideoBean>() {
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

}
