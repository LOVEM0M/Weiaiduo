package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CollegeBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessSchoolContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BusinessSchoolPresenter : BasePresenter<BusinessSchoolContract.IView>(), BusinessSchoolContract.IPresenter {

    override fun getArticleCollegeList(cateId: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("cate_id", "current_page", "page_size")
        val valueArray = arrayOf<Any>(cateId, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.articleCollege(requestBody), object : BaseSingleObserver<CollegeBean>() {
            override fun doOnSuccess(data: CollegeBean) {
                getView()?.showNormal()
                getView()?.getArticleCollegeListSuccess(data)
            }

            override fun doOnFailure(data: CollegeBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    super.doOnFailure(data)
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
