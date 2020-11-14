package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.CouponContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class CouponPresenter : BasePresenter<CouponContract.IView>(), CouponContract.IPresenter {

    override fun getCouponList(currentPage: Int, pageSize: Int, state: Int) {
        val keyArray = arrayOf("current_page", "page_size", "state")
        val valueArray = arrayOf<Any>(currentPage, pageSize, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.couponList(requestBody), object : BaseSingleObserver<CouponBean>() {
            override fun doOnSuccess(data: CouponBean) {
                getView()?.showNormal()
                getView()?.getCouponListSuccess(data)
            }

            override fun doOnFailure(data: CouponBean) {
                if (currentPage == 1 && data.code == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }

}
