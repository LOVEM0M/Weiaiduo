package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean

class ManagerShopContract {

    interface IView : IBaseView {
        fun getMerchantGoodsListSuccess(bean: MerchantGoodsBean)

        fun updateMerchantGoodsStateSuccess(state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantGoodsList(currentPage: Int, pageSize: Int, state: Int)

        fun updateMerchantGoodsState(goodsId: Int, state: Int)
    }

}