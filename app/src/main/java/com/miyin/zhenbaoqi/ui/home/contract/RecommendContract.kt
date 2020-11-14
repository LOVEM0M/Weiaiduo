package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*

class RecommendContract {

    interface IView :IBaseView {
        fun canGetCouponSuccess(bean:CouponGetBean)

        fun getHomeBannerSuccess(bean:HomeBannerBean)

        fun getHotListSuccess(bean:LiveHotBean)

        fun getHomeGoodsHotListSuccess(bean:HomeGoodsHotBean)

        fun liveRoomEntrySuccess(data:LiveEntryRoomBean, position: Int)

        fun bannerClickSuccess(position: Int)

        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun canGetCoupon()

        fun homeBanner()

        fun getHotList()

        fun getHomeGoodsHotList(currentPage: Int, pageSize: Int)

        fun liveRoomEntry(roomId: Int, position: Int)

        fun bannerClick(bannerId: Int, position: Int)
    }

}
