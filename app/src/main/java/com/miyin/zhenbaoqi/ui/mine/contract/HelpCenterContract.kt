package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ArticleBean

class HelpCenterContract {

    interface IView : IBaseView {
        fun getArticleListSuccess(bean: ArticleBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getArticleList(cateId: Int, currentPage: Int, pageSize: Int)
    }

}