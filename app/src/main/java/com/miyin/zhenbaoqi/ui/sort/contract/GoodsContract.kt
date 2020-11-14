package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.NewGoodsBean

class GoodsContract {

    interface IView : IBaseView {
        fun getNewGoodsListSuccess(bean: NewGoodsBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getNewGoodsList(currentPage: Int, pageSize: Int)
    }

}
