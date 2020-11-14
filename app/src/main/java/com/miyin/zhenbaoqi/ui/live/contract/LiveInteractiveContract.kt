package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*

class LiveInteractiveContract {

    interface IView : IBaseView {
        fun getAuctionGoodsSuccess(bean: GoodsDetailBean)
        fun getLiveRoomPopularitySuccess(popularityCount: Int)
        fun closeLiveRoomSuccess()
        fun getMerchantListSuccess()
        fun obtainPeopleLevelSuccess(data: UserGradeBean)
        fun obtainShareListSuccess(data: LiveShareBean)
        fun obtainLiveInfoSuccess(data: MerchantInfoBean)
        fun checkBlackListSuccess(bean: CheckBlackListBean)
        fun openRoomSuccess()
        fun auctionBindGoodsSuccess(bean: BindOfferBean)
        fun shareLiveRoomSuccess(isFriendCircle: Boolean)
        fun getAuctionListSuccess(bean: AuctionGoodsRecordBean, goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAuctionGoods(roomId: Int)
        fun getLiveRoomPopularity(roomId: Int)
        fun closeLiveRoom(roomId: Int)
        fun getMerchantList(merchantId: Int, state: Int)
        fun obtainPeopleLevel()
        fun obtainSharedList()
        fun obtainLiveInfoData(merchantId: Int)
        fun checkBlackList(userId: Int, merchantId: Int)
        fun openRoom(roomId: Int, face_url: String, room_name: String)
        fun auctionBindGoods(goodsId: Int)
        fun shareLiveRoom(room_id: Int, isFriendCircle: Boolean)
        fun getAuctionList(goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int)
    }

}
