package com.miyin.zhenbaoqi.ui.shopcart.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CartGoodsListBean
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.bean.HomeBannerBean
import com.miyin.zhenbaoqi.bean.SearchBean

class ShopCartContract {

    interface IView : IBaseView {
        fun getShopCartListSuccess(bean: CartGoodsListBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getShopCartList(page : Int ,rows : Int)
    }

}
