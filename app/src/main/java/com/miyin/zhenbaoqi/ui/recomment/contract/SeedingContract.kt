package com.miyin.zhenbaoqi.ui.recomment.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.SeedingBean

class SeedingContract {

    interface IView : IBaseView {
        fun getSeedingGoodsListSuccess(bean: SeedingBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getSeedingGoodsList( goodsName:String,currentPage: Int,pageSize: Int)
    }

}
