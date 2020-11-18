package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class LiveContract {

    interface IView : IBaseView {
        fun getCategoryListSuccess(bean: CityBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCategoryList()
    }

}
