package com.miyin.zhenbaoqi.ui.sort.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.SortContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SortPresenter : BasePresenter<SortContract.IView>(), SortContract.IPresenter {

    override fun getCategoryList(codeType: String) {
        val map = ArrayMap<String, Any>().apply {
            put("type", 0)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.showNormal()
                getView()?.getCategoryListSuccess(data)
            }

            override fun doOnFailure(data: CityBean) {
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
                getView()?.showError()
            }
        })
    }

    override fun getRecommend() {
        request(RetrofitUtils.mApiService.recommendList(), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getRecommendSuccess(data)
            }

            override fun doOnFailure(data: CityBean) {
                getView()?.onFailure()
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

    override fun getSecondLevel(parentId: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("parentId", parentId)
        }
        request(RetrofitUtils.mApiService.sonList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getSecondLevelSuccess(data)
            }

            override fun doOnFailure(data: CityBean) {
                getView()?.onFailure()
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

}
