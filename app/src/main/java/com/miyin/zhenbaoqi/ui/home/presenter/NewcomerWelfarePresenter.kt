package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CouponGetBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.WelfareCouponBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.NewcomerWelfareContract

class NewcomerWelfarePresenter :BasePresenter<NewcomerWelfareContract.IView>(), NewcomerWelfareContract.IPresenter {

    override fun canGetCoupon() {
        request(RetrofitUtils.mApiService.welfareCouponCanGet(), object : BaseSingleObserver<CouponGetBean>() {
            override fun doOnSuccess(data: CouponGetBean) {
                getView()?.canGetCouponSuccess(data)
            }
        })
    }

    override fun welfareCoupon() {
        request(RetrofitUtils.mApiService.welfareCouponList(), object : BaseSingleObserver<WelfareCouponBean>() {
            override fun doOnSuccess(data: WelfareCouponBean) {
                getView()?.welfareCouponSuccess(data)
            }
        })
    }

    override fun receiveCoupon() {
        request(RetrofitUtils.mApiService.welfareReceiveCoupon(), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.receiveCouponSuccess()
            }
        })
    }

}
