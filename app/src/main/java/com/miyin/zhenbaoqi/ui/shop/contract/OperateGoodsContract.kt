package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.GoodsDetailBean

class OperateGoodsContract {

    interface IView : IBaseView {
        fun uploadImageSuccess(path: String)

        fun liveGoodsAuctionInsertSuccess(bean: GoodsDetailBean)

        fun liveGoodsSpikeInsertSuccess()

        fun getParentListSuccess(list: List<CityBean.CityListBean>)

        fun getSonListSuccess(list: List<CityBean.CityListBean>, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadImage(path: String)

        fun liveGoodsAuctionInsert(addAmount: Long, cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int,
                                   endTime: Long, ensureAmount: Int, goodsAmount: Long, goodsDescribe: String, goodsFreight: Int,
                                   goodsImg: String, goodsName: String, goodsOriginalAmount: Long, goodsVideo: String,
                                   isSeven: Int, istype: Int, roomId: Int, startAmount: Long, startTime: Long)

        fun liveGoodsSpikeInsert(goodsAmount: Long, goodsDescribe: String, goodsFreight: Int, goodsImg: String?, goodsName: String?,
                                 inventory: Int, isSeven: Int, roomId: Int, isRestriction: Int, goodsId: Int)

        fun getParentList(codeType: String)

        fun getSonList(parentId: Int, type: Int)
    }

}
