package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class SettingContract {

    interface IView : IBaseView {
        fun pushSwitchSuccess(pushSwitch: Int)

        fun updatePushSwitchSuccess(pushSwitch: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun pushSwitch()

        fun updatePushSwitch(pushSwitch: Int)
    }

}
