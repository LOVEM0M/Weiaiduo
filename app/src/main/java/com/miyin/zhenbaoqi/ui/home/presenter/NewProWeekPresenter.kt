package com.miyin.zhenbaoqi.ui.home.presenter

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

    override fun getWeekNewGoodsList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.weekNewGoodsList(requestBody), object : BaseSingleObserver<WeekNewGoodsBean>() {
            override fun doOnSuccess(list: WeekNewGoodsBean) {
                getView()?.showNormal()
                getView()?.getWeekNewGoodsListSuccess(list)
            }

            override fun doOnFailure(list: WeekNewGoodsBean) {
                if (currentPage == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
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
