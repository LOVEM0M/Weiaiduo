package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.bean.takeThreeVipBean

class NewVipContract {

    interface IView : IBaseView {
        fun getVipFirstFreegoodsListSuccess(bean: VipFirstFreegoodsBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getVipFirstFreegoodsList(page: Int, rows: Int)
    }

}
