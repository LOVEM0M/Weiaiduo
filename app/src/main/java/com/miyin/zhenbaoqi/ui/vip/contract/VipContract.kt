package com.miyin.weiaiduo.ui.vip.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class VipContract {

    interface IView : IBaseView {}

    interface IPresenter :IBasePresenter<IView> {}

}
