package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MaterialBean

class MaterialContract {

    interface IView : IBaseView {
        fun getMerchantMaterialListSuccess(bean: MaterialBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantMaterialList(currentPage: Int, pageSize: Int)
    }

}
