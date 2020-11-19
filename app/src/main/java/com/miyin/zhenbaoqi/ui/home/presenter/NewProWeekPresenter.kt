package com.miyin.zhenbaoqi.ui.home.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.FirstCategoryBean
import com.miyin.zhenbaoqi.bean.WeekNewGoodsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.NewProWeekContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class NewProWeekPresenter : BasePresenter<NewProWeekContract.IView>(), NewProWeekContract.IPresenter {
    override fun getCategoryList() {

        request(RetrofitUtils.mApiService.firstCategoryList(), object : BaseSingleObserver<FirstCategoryBean>() {
            override fun doOnSuccess(data: FirstCategoryBean) {
                getView()?.showNormal()
                getView()?.getCategoryListSuccess(data)
            }

            override fun doOnFailure(data: FirstCategoryBean) {
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
                getView()?.showError()
            }
        })
    }

    override fun getWeekNewGoodsList(cateId1: Int,page: Int, rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("cateId1", cateId1)
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.weekNewGoodsList(map), object : BaseSingleObserver<WeekNewGoodsBean>() {
            override fun doOnSuccess(list: WeekNewGoodsBean) {
                getView()?.showNormal()
                getView()?.getWeekNewGoodsListSuccess(list)
            }

            override fun doOnFailure(list: WeekNewGoodsBean) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                    super.doOnFailure(list)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }


}
