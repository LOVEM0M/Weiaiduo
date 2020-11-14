package com.miyin.zhenbaoqi.ui.message.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class NewsContract {

    interface IView : IBaseView {}

    interface IPresenter : IBasePresenter<IView> {}

}
