package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveGoodsBean

class LiveGoodsContract {

    interface IView : IBaseView {
        fun obtainLiveGoodsDataSuccess(bean: LiveGoodsBean)
        fun showEmptyView()
        fun updateShelfGoodsSuccess(state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
       fun obtainLiveGoodsList(currentPage: Int, pageSize: Int, state: String)
        fun updateShelfGoods(goods_id:Int,state:Int)
        fun obtainLiveRoomGoodsList(currentPage: Int, pageSize: Int, state: String,room_id:Int)
    }

}
