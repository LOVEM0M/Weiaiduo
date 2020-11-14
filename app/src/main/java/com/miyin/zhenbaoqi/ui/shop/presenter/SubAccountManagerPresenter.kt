package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.SubAccountInfoBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.SubAccountManagerContract

class SubAccountManagerPresenter : BasePresenter<SubAccountManagerContract.IView>(), SubAccountManagerContract.IPresenter {

    override fun getSubAccountInfo() {
        request(RetrofitUtils.mApiService.merchantsSubList(), object : BaseSingleObserver<SubAccountInfoBean>() {
            override fun doOnSuccess(data: SubAccountInfoBean) {
                getView()?.getSubAccountInfoSuccess(data)
            }
        })
    }

}
