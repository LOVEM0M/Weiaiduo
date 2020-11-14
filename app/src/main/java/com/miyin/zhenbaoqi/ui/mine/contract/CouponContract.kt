package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CouponBean

class CouponContract {

    interface IView : IBaseView {
        fun getCouponListSuccess(bean: CouponBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCouponList(currentPage: Int, pageSize: Int, state: Int)
    }

}
