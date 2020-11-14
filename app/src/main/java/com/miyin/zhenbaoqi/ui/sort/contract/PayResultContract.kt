package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class PayResultContract {

    interface IView : IBaseView {}

    interface IPresenter : IBasePresenter<IView> {}

}