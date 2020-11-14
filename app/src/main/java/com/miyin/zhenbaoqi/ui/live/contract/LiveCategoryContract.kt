package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveBannerBean
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.LiveRoomBean

class LiveCategoryContract {

    interface IView : IBaseView {
        fun getLiveBanner(bean: LiveBannerBean)

        fun getLiveRoomListSuccess(bean: LiveRoomBean)

        fun setEmptyView()

        fun liveRoomEntrySuccess(data: LiveEntryRoomBean, position: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getLiveBanner()

        fun getLiveRoomList(currentPage: Int, pageSize: Int, merchantCate: Int, title: String?)

        fun liveRoomEntry(roomId: Int, position: Int)
    }

}
