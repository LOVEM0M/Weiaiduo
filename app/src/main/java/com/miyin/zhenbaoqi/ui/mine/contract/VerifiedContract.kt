package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.RealNameBean

class VerifiedContract {

    interface IView : IBaseView {
        fun showRealNameSuccess(bean: RealNameBean)

        fun realNameSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun showRealName()

        fun realName(idNo: String?, realName: String?)
    }

}
