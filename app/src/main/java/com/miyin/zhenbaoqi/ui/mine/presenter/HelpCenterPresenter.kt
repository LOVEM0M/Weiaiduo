package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.HelpCenterContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class HelpCenterPresenter : BasePresenter<HelpCenterContract.IView>(), HelpCenterContract.IPresenter {

    override fun getArticleList(cateId: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("cate_id", "current_page", "page_size")
        val valueArray = arrayOf<Any>(cateId, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.articleList(requestBody), object : BaseSingleObserver<ArticleBean>() {
            override fun doOnSuccess(data: ArticleBean) {
                getView()?.showNormal()
                getView()?.getArticleListSuccess(data)
            }

            override fun doOnFailure(data: ArticleBean) {
                if (currentPage == 1 && data.mark == "1") {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.tip)
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
