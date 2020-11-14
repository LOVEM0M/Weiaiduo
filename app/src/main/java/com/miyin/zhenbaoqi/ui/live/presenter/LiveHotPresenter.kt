package com.miyin.zhenbaoqi.ui.live.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LiveHotBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveHotContract

class LiveHotPresenter : BasePresenter<LiveHotContract.IView>(), LiveHotContract.IPresenter {
    override fun obtainHotList(currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.obtainHotList(currentPage, pageSize), object : BaseSingleObserver<LiveHotBean>() {
            override fun doOnSuccess(data: LiveHotBean) {
                getView()?.showNormal()
                getView()?.obtainHotListSuccess(data)
            }

            override fun doOnFailure(data: LiveHotBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
                    getView()?.showEmptyView()
                } else {
                    getView()?.showToast(data.code.toString())
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(e.message)
                }
            }
        })
    }

}
