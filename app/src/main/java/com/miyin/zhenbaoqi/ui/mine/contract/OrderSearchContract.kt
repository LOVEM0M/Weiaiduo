package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.OrderBean

class OrderSearchContract {

    interface IView : IBaseView {
        fun searchGoodsSuccess(bean: OrderBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun searchGoods(currentPage: Int, pageSize: Int, keyWord: String?)
    }

}