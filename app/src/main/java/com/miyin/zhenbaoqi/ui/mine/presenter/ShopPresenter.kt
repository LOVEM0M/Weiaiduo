package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ShopAttentionBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.ShopContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ShopPresenter : BasePresenter<ShopContract.IView>(), ShopContract.IPresenter {

    override fun merchantList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantList(requestBody), object : BaseSingleObserver<ShopAttentionBean>() {
            override fun doOnSuccess(data: ShopAttentionBean) {
                getView()?.showNormal()
                getView()?.merchantListSuccess(data)
            }

            override fun doOnFailure(data: ShopAttentionBean) {
                if (data.mark == "1") {
                    if (currentPage == 1) {
                        getView()?.showEmpty()
                    } else {
                        getView()?.showToast(data.tip)
                    }
                } else {
                    getView()?.showToast(data.tip)
                    getView()?.showError()
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
