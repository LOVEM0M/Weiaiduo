package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class LeakDetectionContract {

    interface IView : IBaseView {}

    interface IPresenter :IBasePresenter<IView> {}

}
