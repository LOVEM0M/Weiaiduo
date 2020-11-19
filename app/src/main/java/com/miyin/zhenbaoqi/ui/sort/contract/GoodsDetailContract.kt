package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.GoodsDetailBean

class GoodsDetailContract {

    interface IView : IBaseView {
        fun getGoodsDetailSuccess(bean: GoodsDetailBean)

        fun updateCollectStateSuccess(collectState: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getGoodsDetail(goodsId: Int)

        fun updateCollectState(goodsId: Int, collectState: Int)

        fun insertFootprint(goodsId: Int, goodsImg: String, goodsAmount: Int)
    }

}
