package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CollegeBean

class BusinessSchoolContract {

    interface IView : IBaseView {
        fun getArticleCollegeListSuccess(bean: CollegeBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getArticleCollegeList(cateId: Int, currentPage: Int, pageSize: Int)
    }

}