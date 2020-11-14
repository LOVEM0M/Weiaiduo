package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CouponGetBean
import com.miyin.zhenbaoqi.bean.WelfareCouponBean

class NewcomerWelfareContract {

    interface IView : IBaseView {
        fun canGetCouponSuccess(bean:CouponGetBean)

        fun welfareCouponSuccess(bean: WelfareCouponBean)

        fun receiveCouponSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun canGetCoupon()

        fun welfareCoupon()

        fun receiveCoupon()
    }

}
