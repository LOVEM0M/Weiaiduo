package com.miyin.zhenbaoqi.ui.live.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveShareContract

class LiveSharePresenter : BasePresenter<LiveShareContract.IView>(), LiveShareContract.IPresenter {
    override fun obtainSharedList(currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.obtainShareList(currentPage, pageSize), object : BaseSingleObserver<LiveShareBean>() {
            override fun doOnSuccess(data: LiveShareBean) {
                getView()?.showNormal()
                getView()?.obtainShareListSuccess(data)
            }

            override fun doOnFailure(data: LiveShareBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
                    getView()?.showEmptyView()
                } else {
                    getView()?.showToast(data.mark)
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
