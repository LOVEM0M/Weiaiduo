package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class LuckyBagDetailContract {

    interface IView : IBaseView {}

    interface IPresenter : IBasePresenter<IView> {}

}
