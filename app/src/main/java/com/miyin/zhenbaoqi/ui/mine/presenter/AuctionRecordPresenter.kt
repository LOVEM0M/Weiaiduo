package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.AuctionRecoredBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AuctionRecordContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AuctionRecordPresenter : BasePresenter<AuctionRecordContract.IView>(), AuctionRecordContract.IPresenter {

    override fun getAuctionRecord(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.auctionBindList(requestBody), object : BaseSingleObserver<AuctionRecoredBean>() {
            override fun doOnSuccess(data: AuctionRecoredBean) {
                getView()?.showNormal()
                getView()?.getAuctionRecordSuccess(data)
            }

            override fun doOnFailure(data: AuctionRecoredBean) {
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
