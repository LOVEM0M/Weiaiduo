package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AuctionGoodsBean
import com.miyin.zhenbaoqi.bean.AuctionGoodsRecordBean
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.bean.PayResultBean

class AuctionDetailContract {

    interface IView : IBaseView {
        fun getAuctionGoodsDetailSuccess(bean: GoodsDetailBean)

        fun getAuctionGoodsRecordSuccess(bean: AuctionGoodsRecordBean)

        fun getAuctionGoodsListSuccess(bean: AuctionGoodsBean)

        fun auctionSecurityDepositSuccess(bean: PayResultBean, payType: Int)

        fun auctionBindGoodsSuccess()

        fun callHandleGoodsSuccess()

        fun updateMerchantStateSuccess(focusState: Int)

        fun onFailure(msg: String, flag: Int)

        fun updateCollectStateSuccess(collectState: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAuctionGoodsDetail(goodsId: Int)

        fun getAuctionGoodsRecord(goodsId: Int, currentPage: Int, pageSize: Int)

        fun getAuctionGoodsList(goodsId: Int)

        fun auctionSecurityDeposit(goodsId: Int, payType: Int)

        fun auctionBindGoods(goodsId: Int)

        fun callHandleGoods()

        fun updateMerchantState(focusState: Int, merchantId: Int)

        fun updateCollectState(goodsId: Int, collectState: Int)
    }

}