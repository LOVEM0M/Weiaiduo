package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class PreferredContract {

    interface IView : com.miyin.zhenbaoqi.base.mvp.IBaseView {}

    interface IPresenter : com.miyin.zhenbaoqi.base.mvp.IBasePresenter<IView> {}

}
