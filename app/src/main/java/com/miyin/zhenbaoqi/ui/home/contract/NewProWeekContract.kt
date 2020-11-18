package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.FirstCategoryBean
import com.miyin.zhenbaoqi.bean.WeekNewGoodsBean

class NewProWeekContract {

    interface IView : IBaseView {
        fun getCategoryListSuccess(bean: FirstCategoryBean)
        fun getWeekNewGoodsListSuccess(bean: WeekNewGoodsBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter :IBasePresenter<IView> {
        fun getCategoryList()
        fun getWeekNewGoodsList(currentPage: Int, pageSize: Int)
    }

}
