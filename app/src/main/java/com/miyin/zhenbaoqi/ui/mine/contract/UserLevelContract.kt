package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.UserGradeBean

class UserLevelContract {

    interface IView : IBaseView {
        fun getUserLevelSuccess(bean: ResponseBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getUserLevel()
    }

}
