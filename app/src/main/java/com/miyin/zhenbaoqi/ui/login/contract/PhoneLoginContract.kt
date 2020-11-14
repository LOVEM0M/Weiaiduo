package com.miyin.zhenbaoqi.ui.login.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class PhoneLoginContract {

    interface IView : IBaseView {
        fun setCodeBtnEnabled(enabled: Boolean)

        fun setCodeBtnText(title: String)

        fun onLogin()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun sendVerificationCode(phone: String?)

        fun login(code: String?, phone: String?, pushCode: String?, inviteCode: String?)
    }

}
