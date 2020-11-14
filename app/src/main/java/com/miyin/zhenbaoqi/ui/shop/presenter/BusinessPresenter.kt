package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessContract

class BusinessPresenter : BasePresenter<BusinessContract.IView>(), BusinessContract.IPresenter {

    override fun getArticleActivity(currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.articleActivity(currentPage, pageSize), object : BaseSingleObserver<ArticleBean>() {
            override fun doOnSuccess(data: ArticleBean) {
                getView()?.showNormal()
                getView()?.getArticleActivitySuccess(data)
            }

            override fun doOnFailure(data: ArticleBean) {
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
