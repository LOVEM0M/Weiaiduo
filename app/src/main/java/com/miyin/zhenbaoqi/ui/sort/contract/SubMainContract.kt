package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class SubMainContract {

    interface IView : IBaseView {
        fun liveRoomEntrySuccess(isShowLiveGoods: Boolean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun liveRoomEntry(roomId: Int, isShowLiveGoods: Boolean)
    }

}
