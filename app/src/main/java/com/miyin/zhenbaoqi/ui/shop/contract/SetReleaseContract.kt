package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class SetReleaseContract {

    interface IView : IBaseView {
        fun addMerchantGoodsSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun addMerchantGoods(cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int, endTime: Long,
                             goodsAmount: String?, goodsDescribe: String?, goodsFreight: Int, goodsImg: String?,
                             goodsName: String?, goodsOriginalAmount: String?, goodsType: Int, goodsVideo: String?,
                             inventory: Int, isRestriction: Int, isSeven: Int, goodsId: Int, measure: String,
                             texture: String, place: String, weight: String)
    }

}
