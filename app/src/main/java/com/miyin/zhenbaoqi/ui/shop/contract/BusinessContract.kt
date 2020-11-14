package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ArticleBean

class BusinessContract {

    interface IView : IBaseView {
        fun getArticleActivitySuccess(bean: ArticleBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getArticleActivity(currentPage: Int, pageSize: Int)
    }

}
