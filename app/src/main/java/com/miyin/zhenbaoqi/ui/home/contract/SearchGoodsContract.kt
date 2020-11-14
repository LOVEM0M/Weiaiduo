package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.GoodsSearchBean

class SearchGoodsContract {

    interface IView : com.miyin.zhenbaoqi.base.mvp.IBaseView {
        fun searchGoodsSuccess(bean:GoodsSearchBean)
    }

    interface IPresenter : com.miyin.zhenbaoqi.base.mvp.IBasePresenter<IView> {
        fun searchGoods(currentPage: Int, pageSize: Int, param: String, type: Int)
    }

}