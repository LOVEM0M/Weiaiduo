package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class SortContract {

    interface IView : IBaseView {
        fun getCategoryListSuccess(bean: CityBean)

        fun getRecommendSuccess(bean: CityBean)

        fun getSecondLevelSuccess(bean: CityBean)

        fun onFailure()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCategoryList(codeType: String)

        fun getRecommend()

        fun getSecondLevel(parentId: Int)
    }

}
