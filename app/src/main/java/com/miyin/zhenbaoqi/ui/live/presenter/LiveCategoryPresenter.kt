package com.miyin.zhenbaoqi.ui.live.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LiveBannerBean
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.LiveRoomBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveCategoryContract

class LiveCategoryPresenter : BasePresenter<LiveCategoryContract.IView>(), LiveCategoryContract.IPresenter {

    override fun getLiveBanner() {
        request(RetrofitUtils.mApiService.liveBanner(), object : BaseSingleObserver<LiveBannerBean>() {
            override fun doOnSuccess(data: LiveBannerBean) {
                getView()?.getLiveBanner(data)
            }

            override fun doOnFailure(data: LiveBannerBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

    override fun getLiveRoomList(currentPage: Int, pageSize: Int, merchantCate: Int, title: String?) {
        request(RetrofitUtils.mApiService.liveRoomList(currentPage, pageSize, merchantCate), object : BaseSingleObserver<LiveRoomBean>() {
            override fun doOnSuccess(data: LiveRoomBean) {
                getView()?.showNormal()
                getView()?.getLiveRoomListSuccess(data)
            }

            override fun doOnFailure(data: LiveRoomBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.code.toString())
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

    override fun liveRoomEntry(roomId: Int, position: Int) {
        request(RetrofitUtils.mApiService.liveRoomEntry(roomId), object : BaseSingleObserver<LiveEntryRoomBean>() {
            override fun doOnSuccess(data: LiveEntryRoomBean) {
                getView()?.liveRoomEntrySuccess(data, position)
            }
        })
    }

}
