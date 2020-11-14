package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class SetAuctionContract {

    interface IView : IBaseView {
        fun addAuctionGoodsSuccess()

        fun saveAuctionGoodsSuccess()

        fun onFailure(msg: String, flag: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun addAuctionGoods(addAmount: Long, cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int, endTime: Long,
                            ensureAmount: Int, goodsAmount: Long, goodsDescribe: String?, goodsFreight: Int, goodsImg: String?,
                            goodsName: String?, goodsOriginalAmount: Long, goodsVideo: String?, isSeven: Int, startAmount: Long,
                            startTime: Long, measure: String, texture: String, place: String, weight: String)

        fun saveAuctionGoods(json: String)
    }

}