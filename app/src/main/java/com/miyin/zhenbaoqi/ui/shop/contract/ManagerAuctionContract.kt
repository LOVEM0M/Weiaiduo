package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean

class ManagerAuctionContract {

    interface IView : IBaseView {
        fun getAuctionMerchantGoodsListSuccess(bean: MerchantGoodsBean)

        fun getDraftAuctionGoodsListSuccess()

        fun updateMerchantGoodsStateSuccess(state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAuctionMerchantGoodsList(auctionState: Int, currentPage: Int, pageSize: Int)

        fun getDraftAuctionGoodsList(currentPage: Int, pageSize: Int)

        fun updateMerchantGoodsState(goodsId: Int, state: Int)
    }

}