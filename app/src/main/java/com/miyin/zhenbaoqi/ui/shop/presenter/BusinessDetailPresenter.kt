package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessDetailContract

class BusinessDetailPresenter : BasePresenter<BusinessDetailContract.IView>(), BusinessDetailContract.IPresenter {

    override fun joinActivity(activityId: Int) {
        request(RetrofitUtils.mApiService.joinActivity(activityId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.joinActivitySuccess()
            }
        })
    }

    override fun cancelActivity(activityId: Int) {
        request(RetrofitUtils.mApiService.cancelActivity(activityId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.cancelActivitySuccess()
            }
        })
    }

}
