package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*
import java.io.File

class PushLiveContract {

    interface IView : IBaseView {
        fun uploadLiveRoomCoverSuccess()

        fun closeLiveRoomSuccess()

        fun obtainShareListSuccess(data: LiveShareBean)

        fun liveRoomCreateSuccess(bean: LiveRoomCreateBean)

        fun liveRoomOpenSuccess(bean: LiveRoomOpenBean)

        fun uploadMerchantHeadSuccess(path: String)

        fun obtainPeopleLevelSuccess(data: UserGradeBean)

        fun shareLiveRoomSuccess(isFriendCircle: Boolean)

        fun getAuctionListSuccess(bean: AuctionGoodsRecordBean, goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadLiveRoomCover(roomId: Int, file: File)

        fun closeLiveRoom(roomId: Int)

        fun obtainSharedList(currentPage: Int, pageSize: Int)

        fun liveRoomCreate()

        fun liveRoomOpen(roomId: Int, face_url: String?, room_name: String?)

        fun uploadMerchantHead(path: String)

        fun obtainPeopleLevel()

        fun shareLiveRoom(roomId: Int, isFriendCircle: Boolean)

        fun getAuctionList(goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int)
    }

}
