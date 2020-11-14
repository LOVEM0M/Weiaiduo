package com.miyin.zhenbaoqi.ui.shopcart.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.bean.SearchBean

class ShopCartContract {

    interface IView : IBaseView {}

    interface IPresenter : IBasePresenter<IView> {}

}
