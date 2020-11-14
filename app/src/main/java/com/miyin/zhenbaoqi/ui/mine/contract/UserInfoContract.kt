package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ReferrerBean

class UserInfoContract {

    interface IView : IBaseView {
        fun updateHeadImgSuccess(path: String)

        fun updateUserInfoSuccess()

        fun referrerInfoSuccess(bean: ReferrerBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun updateHeadImg(path: String)

        fun updateUserInfo(birthday: String?, gender: Int, nickName: String?)

        fun referrerInfo()
    }

}
