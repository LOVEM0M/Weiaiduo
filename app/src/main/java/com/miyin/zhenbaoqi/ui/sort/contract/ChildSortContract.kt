package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class ChildSortContract {

    interface IView : IBaseView {
        fun getRecommendSuccess(bean: CityBean)

        fun getSecondLevelSuccess(bean: CityBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getRecommend()

        fun getSecondLevel(parentId: Int)
    }

}