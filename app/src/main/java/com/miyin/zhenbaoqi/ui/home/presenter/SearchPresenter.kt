package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.SearchBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.SearchContract

class SearchPresenter : BasePresenter<SearchContract.IView>(), SearchContract.IPresenter {

    override fun getGoodsSearchList() {
        request(RetrofitUtils.mApiService.goodsSearchList(), object : BaseSingleObserver<SearchBean>() {
            override fun doOnSuccess(data: SearchBean) {
                getView()?.getGoodsSearchListSuccess(data)
            }

            override fun doOnFailure(data: SearchBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

}
