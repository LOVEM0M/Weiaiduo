package com.miyin.zhenbaoqi.ui.message.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.message.contract.MessageContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class MessagePresenter : BasePresenter<MessageContract.IView>(), MessageContract.IPresenter {

    override fun getMessageList(messageType: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("message_type", "current_page", "page_size")
        val valueArray = arrayOf<Any>(messageType, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.messageList(requestBody), object : BaseSingleObserver<MessageBean>() {
            override fun doOnSuccess(data: MessageBean) {
                getView()?.showNormal()
                getView()?.getMessageListSuccess(data)
            }

            override fun doOnFailure(data: MessageBean) {
                if (currentPage == 1) {
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

    override fun notReadMessageSize() {
        request(RetrofitUtils.mApiService.notReadMessageSize(), object : BaseSingleObserver<MessageBean>() {
            override fun doOnSuccess(data: MessageBean) {
                getView()?.notReadMessageSize(data)
            }

            override fun doOnFailure(data: MessageBean) {
                getView()?.showToast(data.tip)
            }
        })
    }

    override fun searchMerchantId(userId: Int) {
        request(RetrofitUtils.mApiService.searchMerchantsId(userId), object : BaseSingleObserver<UserTypeBean>() {
            override fun doOnSuccess(data: UserTypeBean) {
                getView()?.searchMerchantIdSuccess(data)
            }
        })
    }

}
