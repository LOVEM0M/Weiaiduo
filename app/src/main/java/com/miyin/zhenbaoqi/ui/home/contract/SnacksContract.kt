package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.bean.SearchBean

class SnacksContract {

    interface IView : IBaseView {}

    interface IPresenter : IBasePresenter<IView> {}

}
