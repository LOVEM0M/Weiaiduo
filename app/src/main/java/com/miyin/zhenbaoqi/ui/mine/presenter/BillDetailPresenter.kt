package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BillPayBean
import com.miyin.zhenbaoqi.bean.BillWithdrawBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.BillDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BillDetailPresenter : BasePresenter<BillDetailContract.IView>(), BillDetailContract.IPresenter {

    override fun withdrawDetail(changeNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("change_number"), arrayOf(changeNumber))
        request(RetrofitUtils.mApiService.billWithdrawDetail(requestBody), object : BaseSingleObserver<BillWithdrawBean>() {
            override fun doOnSuccess(data: BillWithdrawBean) {
                getView()?.withdrawDetailSuccess(data)
            }
        })
    }

    override fun incomeDetail(changeNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("change_number"), arrayOf(changeNumber))
        request(RetrofitUtils.mApiService.billIncomeDetail(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.incomeDetailSuccess()
            }
        })
    }

    override fun payDetail(changeNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("change_number"), arrayOf(changeNumber))
        request(RetrofitUtils.mApiService.billPayDetail(requestBody), object : BaseSingleObserver<BillPayBean>() {
            override fun doOnSuccess(data: BillPayBean) {
                getView()?.payDetailSuccess(data)
            }
        })
    }

}
