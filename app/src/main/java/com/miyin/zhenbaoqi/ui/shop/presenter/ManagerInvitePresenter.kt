package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.InvitePlayerBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerInviteContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils

class ManagerInvitePresenter : BasePresenter<ManagerInviteContract.IView>(), ManagerInviteContract.IPresenter {

    override fun inviteExclusiveFansList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size", "user_id")
        val valueArray = arrayOf<Any>(currentPage, pageSize, SPUtils.getInt("user_id"))
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.inviteExclusiveFansList(requestBody), object : BaseSingleObserver<InvitePlayerBean>() {
            override fun doOnSuccess(data: InvitePlayerBean) {
                getView()?.showNormal()
                getView()?.getInviteListSuccess(data)
            }

            override fun doOnFailure(data: InvitePlayerBean) {
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

    override fun inviteOrdinaryFansList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size", "user_id")
        val valueArray = arrayOf<Any>(currentPage, pageSize, SPUtils.getInt("user_id"))
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.inviteOrdinaryFansList(requestBody), object : BaseSingleObserver<InvitePlayerBean>() {
            override fun doOnSuccess(data: InvitePlayerBean) {
                getView()?.showNormal()
                getView()?.getInviteListSuccess(data)
            }

            override fun doOnFailure(data: InvitePlayerBean) {
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

    override fun invitePlayerList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size", "user_id")
        val valueArray = arrayOf<Any>(currentPage, pageSize, SPUtils.getInt("user_id"))
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.invitePlayerList(requestBody), object : BaseSingleObserver<InvitePlayerBean>() {
            override fun doOnSuccess(data: InvitePlayerBean) {
                getView()?.showNormal()
                getView()?.getInviteListSuccess(data)
            }

            override fun doOnFailure(data: InvitePlayerBean) {
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
