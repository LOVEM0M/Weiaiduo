package com.miyin.zhenbaoqi.ui.message.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.NoticeBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.message.contract.OfficialNotificationContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class OfficialNotificationPresenter : BasePresenter<OfficialNotificationContract.IView>(), OfficialNotificationContract.IPresenter {

    override fun getNoticeList(cateId: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("cate_id", "current_page", "page_size")
        val valueArray = arrayOf<Any>(cateId, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.noticeList(requestBody), object : BaseSingleObserver<NoticeBean>() {
            override fun doOnSuccess(data: NoticeBean) {
                getView()?.showNormal()
                getView()?.getNoticeListSuccess(data)
            }

            override fun doOnFailure(data: NoticeBean) {
                if (currentPage == 1 && data.code == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
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
