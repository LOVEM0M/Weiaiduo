package com.miyin.zhenbaoqi.ui.sort.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.ChildSortContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ChildSortPresenter : BasePresenter<ChildSortContract.IView>(), ChildSortContract.IPresenter {

    override fun getRecommend() {
        request(RetrofitUtils.mApiService.recommendList(), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getRecommendSuccess(data)
            }

            override fun doOnFailure(data: CityBean) {

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

            }
        })
    }

}
