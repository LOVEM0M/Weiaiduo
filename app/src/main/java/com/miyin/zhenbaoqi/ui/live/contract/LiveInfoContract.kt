package com.miyin.zhenbaoqi.ui.live.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveGoodsBean

class LiveInfoContract {

    interface IView : IBaseView {
    }

    interface IPresenter : IBasePresenter<IView> {
    }

}
