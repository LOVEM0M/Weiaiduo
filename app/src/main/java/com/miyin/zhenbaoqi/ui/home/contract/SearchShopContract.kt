package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.SearchShopBean

class SearchShopContract {

    interface IView : IBaseView {
        fun searchShopSuccess(bean: SearchShopBean)
    }

    interface IPresenter : com.miyin.zhenbaoqi.base.mvp.IBasePresenter<IView> {
        fun searchShop(currentPage: Int, pageSize: Int, searchParam: String)
    }

}