package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ReportBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantReportContract

class MerchantReportPresenter : BasePresenter<MerchantReportContract.IView>(), MerchantReportContract.IPresenter {

    override fun merchantsStatistics(type: Int) {
        request(RetrofitUtils.mApiService.merchantReport(type), object : BaseSingleObserver<ReportBean>() {
            override fun doOnSuccess(data: ReportBean) {
                getView()?.merchantsStatisticsSuccess(data)
            }
        })
    }

}
