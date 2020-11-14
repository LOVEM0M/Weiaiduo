package com.miyin.zhenbaoqi.ui.sort.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.SubMainContract

class SubMainPresenter : BasePresenter<SubMainContract.IView>(), SubMainContract.IPresenter {

    override fun liveRoomEntry(roomId: Int, isShowLiveGoods: Boolean) {
        request(RetrofitUtils.mApiService.liveRoomEntry(roomId), object : BaseSingleObserver<LiveEntryRoomBean>() {
            override fun doOnSuccess(data: LiveEntryRoomBean) {
                getView()?.liveRoomEntrySuccess(isShowLiveGoods)
            }
        })
    }

}
