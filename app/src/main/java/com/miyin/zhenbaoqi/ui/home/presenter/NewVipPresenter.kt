package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.RestoreBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.BargainContract
import com.miyin.zhenbaoqi.ui.home.contract.NewVipContract
import com.miyin.zhenbaoqi.ui.home.contract.SignInContract
import com.miyin.zhenbaoqi.ui.home.contract.SnacksContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class NewVipPresenter :BasePresenter<NewVipContract.IView>(), NewVipContract.IPresenter {
    override fun getRestoreList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.restoreList(requestBody), object : BaseSingleObserver<RestoreBean>() {
            override fun doOnSuccess(list: RestoreBean) {
                getView()?.showNormal()
                getView()?.getRestoreListSuccess(list)
            }

            override fun doOnFailure(list: RestoreBean) {
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
