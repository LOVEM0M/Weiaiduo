package com.miyin.zhenbaoqi.ui.live.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class LivePresenter : BasePresenter<LiveContract.IView>(), LiveContract.IPresenter {

    override fun getCategoryList() {
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf("goods_category"))
        request(RetrofitUtils.mApiService.parentList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.showNormal()
                getView()?.getCategoryListSuccess(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

}
