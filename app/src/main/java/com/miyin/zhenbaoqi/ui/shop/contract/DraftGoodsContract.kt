package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity

class DraftGoodsContract {

    interface IView : IBaseView {
        fun getDraftGoodsListSuccess(list: List<AuctionGoodsDraftEntity>)

        fun deleteDraftGoodsSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getDraftGoodsList(currentPage: Int, pageSize: Int)

        fun deleteDraftGoods(id: Long)
    }

}