package com.miyin.zhenbaoqi.ui.login.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LoginBean
import com.miyin.zhenbaoqi.bean.UserSignBean

class WXLoginContract {

    interface IView : IBaseView {
//        fun onWXLoginSuccess(json: String)

//        fun appLoginSuccess(bean: LoginBean)

//        fun getUserSignSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
//        fun wxLogin(code: String?)

//        fun appLogin(city: String, country: String, gender: String, headImg: String, nickName: String,
//                     openId: String, province: String, unionId: String)

//        fun getUserSign()
    }

}
