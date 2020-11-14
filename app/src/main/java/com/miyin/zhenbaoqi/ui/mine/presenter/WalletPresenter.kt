package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BillBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.WalletContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class WalletPresenter : BasePresenter<WalletContract.IView>(), WalletContract.IPresenter {

    override fun getBillList(changeType: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("change_type", "current_page", "page_size")
        val valueArray = arrayOf<Any>(changeType, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.billList(requestBody), object : BaseSingleObserver<BillBean>() {
            override fun doOnSuccess(data: BillBean) {
                getView()?.showNormal()
                getView()?.getBillListSuccess(data)
            }

            override fun doOnFailure(data: BillBean) {
                if (currentPage == 1 && data.mark == "1") {
                    getView()?.showEmpty()
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
