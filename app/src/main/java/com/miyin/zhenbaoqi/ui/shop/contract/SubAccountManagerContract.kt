package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.MerchantBean
import com.miyin.zhenbaoqi.bean.SubAccountInfoBean

class SubAccountManagerContract {

    interface IView : IBaseView {
        fun getSubAccountInfoSuccess(bean: SubAccountInfoBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getSubAccountInfo()
    }

}